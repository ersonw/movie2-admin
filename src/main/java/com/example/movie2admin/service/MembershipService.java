package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MembershipService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsersService usersService;
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private CashInOptionDao cashInOptionDao;
    @Autowired
    private MembershipConfigDao membershipConfigDao;
    @Autowired
    private MembershipBenefitDao membershipBenefitDao;
    @Autowired
    private MembershipButtonDao membershipButtonDao;
    @Autowired
    private MembershipExperienceDao membershipExperienceDao;
    @Autowired
    private MembershipExpiredDao membershipExpiredDao;
    @Autowired
    private MembershipFundsDao membershipFundsDao;
    @Autowired
    private MembershipGradeDao membershipGradeDao;
    @Autowired
    private MembershipLevelDao membershipLevelDao;
    @Autowired
    private MembershipOrderDao membershipOrderDao;

    public boolean getConfigBool(String name) {
        return getConfigLong(name) > 0;
    }
    public long getConfigLong(String name) {
        String value = getConfig(name);
        if (value == null) return 0;
        return Long.parseLong(value);
    }
    public double getConfigDouble(String name) {
        String value = getConfig(name);
        if (value == null) return 0;
        return Long.parseLong(value);
    }
    public String getConfig(String name) {
        List<MembershipConfig> configs = membershipConfigDao.findAllByName(name);
        return configs.isEmpty() ? null : configs.get(0).getVal();
    }

    public ResponseData getMembershipList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<MembershipExpired> expiredPage;
        if (StringUtils.isNotEmpty(title)) {
            expiredPage = membershipExpiredDao.getMembershipList("%"+title+"%",pageable);
        }else {
            expiredPage = membershipExpiredDao.getMembershipList(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipExpired expired : expiredPage.getContent()) {
            array.add(getMembership(expired));
        }
        JSONObject object = ResponseData.object("total", expiredPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getMembership(MembershipExpired expired){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(expired));
        json.put("expired", expired.getExpired()+expired.getAddTime());
        json.put("user", "用户不存在");
        json.put("level", 0);
        json.put("orders", 0);
        json.put("experience", 0);
        json.put("experiences", 0);
        User user = userDao.findAllById(expired.getUserId());
        if(user != null){
            json.put("user", user.getUsername());
            json.put("level", usersService.getMemberLevel(user.getId()));
            json.put("experience", membershipExperienceDao.getAllByUserId(user.getId()));
            json.put("experiences", membershipExperienceDao.countByUserId(user.getId()));
            json.put("orders", membershipOrderDao.getOrders(user.getId()));
        }
        return json;
    }

    public ResponseData deleteMembership(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipExpiredDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData expiredMembership(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<MembershipExpired> expireds = membershipExpiredDao.findAllById(ids);
        for (int i = 0; i < expireds.size(); i++) {
            expireds.get(i).setExpired(0);
        }
        membershipExpiredDao.saveAll(expireds);
        JSONArray array = new JSONArray();
        for (MembershipExpired expired : expireds) {
            array.add(getMembership(expired));
        }
        JSONObject object = ResponseData.object("list", array);
        return ResponseData.success(object);
    }

    public ResponseData getMembershipListOrder(long userId, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        if(userId < 0) return ResponseData.error("用户不存在");
        if (userDao.findAllById(userId) == null) return ResponseData.error("用户不存在");
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<MembershipFunds> fundsPage;
        if (start > 0 && end > 0){
            fundsPage = membershipFundsDao.findAllByUserIdAndAddTimeGreaterThanEqualAndAddTimeLessThanEqual(userId, start, end, pageable);
        }else if (start > 0){
            fundsPage = membershipFundsDao.findAllByUserIdAndAddTimeGreaterThanEqual(userId,start,pageable);
        }else if (end > 0){
            fundsPage = membershipFundsDao.findAllByUserIdAndAddTimeLessThanEqual(userId,end,pageable);
        }else{
            fundsPage = membershipFundsDao.findAllByUserId(userId,pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipFunds funds : fundsPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(funds));
            array.add(json);
        }
        JSONObject object = ResponseData.object("list", array);
        object.put("total", fundsPage.getTotalElements());
        return ResponseData.success(object);
    }

    public ResponseData deleteMembershipListOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipFundsDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData getMembershipListExperience(long userId, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        if(userId < 0) return ResponseData.error("用户不存在");
        if (userDao.findAllById(userId) == null) return ResponseData.error("用户不存在");
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<MembershipExperience> experiencePage;
        if (start > 0 && end > 0){
            experiencePage = membershipExperienceDao.findAllByUserIdAndAddTimeGreaterThanEqualAndAddTimeLessThanEqual(userId, start, end, pageable);
        }else if (start > 0){
            experiencePage = membershipExperienceDao.findAllByUserIdAndAddTimeGreaterThanEqual(userId,start,pageable);
        }else if (end > 0){
            experiencePage = membershipExperienceDao.findAllByUserIdAndAddTimeLessThanEqual(userId,end,pageable);
        }else{
            experiencePage = membershipExperienceDao.findAllByUserId(userId,pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipExperience experience : experiencePage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(experience));
            array.add(json);
        }
        JSONObject object = ResponseData.object("list", array);
        object.put("total", experiencePage.getTotalElements());
        return ResponseData.success(object);
    }

    public ResponseData deleteMembershipListExperience(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipExperienceDao.deleteAllById(ids);
        return ResponseData.success();
    }
}
