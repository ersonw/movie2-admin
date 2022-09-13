package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.CashInConfigDao;
import com.example.movie2admin.dao.CashInOptionDao;
import com.example.movie2admin.dao.CashInOrderDao;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.CashInConfig;
import com.example.movie2admin.entity.CashInOption;
import com.example.movie2admin.entity.MembershipExpired;
import com.example.movie2admin.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RechargeService {
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private CashInOptionDao cashInOptionDao;
    @Autowired
    private CashInConfigDao cashInConfigDao;
    public ResponseData getChannelList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<CashInConfig> configPage;
        if (StringUtils.isNotEmpty(title)) {
            configPage = cashInConfigDao.findAllByTitleLike("%"+title+"%",pageable);
        }else {
            configPage = cashInConfigDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (CashInConfig config : configPage.getContent()) {
            array.add(getConfig(config));
        }
        JSONObject object = ResponseData.object("total", configPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getConfig(CashInConfig config){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(config));
        object.put("options",getAllowed(config.getAllowed()));
        return object;
    }

    public ResponseData deleteChannel(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        cashInConfigDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateChannel(long id, String title, String domain, String mchId, String secretKey, String callbackUrl, String notifyUrl, String errorUrl,List<Long> options, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(domain) || StringUtils.isEmpty(mchId) || StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(callbackUrl) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(errorUrl)) return ResponseData.error("必要参数不可为空");
        CashInConfig config = cashInConfigDao.findAllById(id);
        if (config == null) return ResponseData.error("记录不存在!");
        title = title.replaceAll(" ","").trim().toUpperCase();
        config.setTitle(title);
        config.setDomain(domain);
        config.setMchId(mchId);
        config.setSecretKey(secretKey);
        config.setCallbackUrl(callbackUrl);
        config.setNotifyUrl(notifyUrl);
        config.setErrorUrl(errorUrl);
        config.setAllowed(getAllowed(options));
        if (config.getAddTime() == 0)config.setAddTime(System.currentTimeMillis());
        config.setUpdateTime(System.currentTimeMillis());
        cashInConfigDao.save(config);
        return ResponseData.success();
    }

    private List<Long> getAllowed(String allowed) {
        List<Long> options = new ArrayList<>();
        if (StringUtils.isNotEmpty(allowed)) {
            String[] alloweds = allowed.split(",");
            for (String s : alloweds) {
                CashInOption option = cashInOptionDao.findAllByName(s);
                if (option!= null) {
                    options.add(option.getId());
                }
            }
        }
        return options;
    }
    private String getAllowed(List<Long> options) {
        StringBuilder sb = new StringBuilder();
        for (Long id : options) {
            CashInOption option = cashInOptionDao.findAllById(id);
            if (option!= null) {
                sb.append(option.getName()).append(",");
            }
        }
        return sb.toString();
    }

    public ResponseData addChannel(String title, String domain, String mchId, String secretKey, String callbackUrl, String notifyUrl, String errorUrl, List<Long> options, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(domain) || StringUtils.isEmpty(mchId) || StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(callbackUrl) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(errorUrl)) return ResponseData.error("必要参数不可为空");
        title = title.replaceAll(" ","").trim().toUpperCase();
        CashInConfig config = new CashInConfig();
        config.setTitle(title);
        config.setDomain(domain);
        config.setMchId(mchId);
        config.setSecretKey(secretKey);
        config.setCallbackUrl(callbackUrl);
        config.setNotifyUrl(notifyUrl);
        config.setErrorUrl(errorUrl);
        config.setAllowed(getAllowed(options));
        config.setAddTime(System.currentTimeMillis());
        config.setUpdateTime(System.currentTimeMillis());
        cashInConfigDao.save(config);
        return ResponseData.success(getConfig(config));
    }

    public ResponseData getOptionList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<CashInOption> optionPage;
        if (StringUtils.isNotEmpty(title)) {
            optionPage = cashInOptionDao.findAllByNameLike("%"+title+"%",pageable);
        }else {
            optionPage = cashInOptionDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (CashInOption option : optionPage.getContent()) {
            array.add(getOption(option));
        }
        JSONObject object = ResponseData.object("total", optionPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getOption(CashInOption option){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(option));
        return object;
    }
}
