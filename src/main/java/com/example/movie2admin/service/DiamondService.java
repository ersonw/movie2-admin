package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.EPayData;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.EPayUtil;
import com.example.movie2admin.util.TimeUtil;
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
public class DiamondService {
    private static int INDEX_OF_CASH_IN = 0;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private UserBalanceDiamondDao userBalanceDiamondDao;
    @Autowired
    private DiamondConfigDao diamondConfigDao;
    @Autowired
    private DiamondButtonDao diamondButtonDao;
    @Autowired
    private DiamondOrderDao diamondOrderDao;
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private CashInConfigDao cashInConfigDao;
    @Autowired
    private CashInOptionDao cashInOptionDao;
    @Autowired
    private UserConsumeDao userConsumeDao;


    public boolean getConfigBool(String name){
        return getConfigLong(name) > 0;
    }
    public long getConfigLong(String name){
        String value = getConfig(name);
        if(value == null) return 0;
        return Long.parseLong(value);
    }
    public String getConfig(String name){
        List<DiamondConfig> configs = diamondConfigDao.findAllByName(name);
        return configs.isEmpty() ? null : configs.get(0).getVal();
    }
    public List<CashInOption> getAllowed(CashInConfig config){
        List<CashInOption> options = cashInOptionDao.findAllByStatus(1);
        if (config != null && StringUtils.isNotEmpty(config.getAllowed())){
            String[] allowed = config.getAllowed().split(",");
            options = new ArrayList<>();
            for (String s : allowed) {
                List<CashInOption> o = cashInOptionDao.findAllByStatusAndName(1, s);
//                    List<CashInOption> o = cashInOptionDao.findAllByStatusAndCode(1, s);
                if (o.size() > 0) {
                    options.add(o.get(0));
                }
            }
        }
        return options;
    }

    public boolean handlerOrder(String orderId){
        DiamondOrder order = diamondOrderDao.findAllByOrderNo(orderId);
        CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(orderId);
        if (order == null || inOrder == null) return false;
        User user = userDao.findAllById(order.getUserId());
        if (user == null) return false;
        UserBalanceDiamond balance = new UserBalanceDiamond();
        balance.setAddTime(System.currentTimeMillis());
        balance.setAmount(order.getAmount());
        balance.setUserId(user.getId());
        balance.setText("在线充值");
        userBalanceDiamondDao.save(balance);
        if (StringUtils.isEmpty(inOrder.getTotalFee())) inOrder.setTotalFee("0.0");
        UserConsume consume = new UserConsume(user.getId(), new Double(inOrder.getTotalFee()).longValue(),"在线充值钻石"+order.getAmount(),1);
        userConsumeDao.saveAndFlush(consume);
        return true;
    }
}
