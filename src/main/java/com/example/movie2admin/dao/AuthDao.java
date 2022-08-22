package com.example.movie2admin.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.EPayData;
import com.example.movie2admin.data.SearchData;
import com.example.movie2admin.data.SmsCode;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AuthDao {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Timer timer = new Timer();
    public void pushOrder(EPayData data){
        EPayData object = findOrderByOrderId(data.getOut_trade_no());
        if (object != null){
            popOrder(object);
        }
        redisTemplate.opsForSet().add("orders",JSONObject.toJSONString(data));
    }
    public EPayData findOrderByOrderId(String orderId){
        Set orders = redisTemplate.opsForSet().members("orders");
        if (orders != null){
            for (Object o: orders) {
                JSONObject jsonObject = JSONObject.parseObject(o.toString());
                if (orderId.equals(jsonObject.get("out_trade_no"))){
                    return JSONObject.toJavaObject(jsonObject,EPayData.class);
                }
            }
        }
        return null;
    }
    public void popOrder(EPayData data){
        redisTemplate.opsForSet().remove("orders" ,JSONObject.toJSONString(data));
    }
    public void popOrderById(String orderId){
        EPayData object = findOrderByOrderId(orderId);
        if (object != null){
            popOrder(object);
        }
    }
    public void pushSearch(SearchData data){
        SearchData object = findSearch(data.getId());
        if (object != null){
            popSearch(object);
        }
        redisTemplate.opsForSet().add("searchs",JSONObject.toJSONString(data));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                popSearch(object);
            }
        }, 1000 * 60 * 60);
    }
    public List<SearchData> findSearchByUserId(long userId){
        Set searchs = redisTemplate.opsForSet().members("searchs");
        if (searchs != null){
            List<SearchData> datas = new ArrayList<>();
            for (Object search: searchs) {
                JSONObject jsonObject = JSONObject.parseObject(search.toString());
                if (jsonObject.getLong("userId") == userId){
                    datas.add(JSONObject.toJavaObject(jsonObject,SearchData.class));
                }
            }
            return datas;
        }
        return null;
    }
    public SearchData findSearch(String id){
        Set searchs = redisTemplate.opsForSet().members("searchs");
        if (searchs != null){
            for (Object search: searchs) {
                JSONObject jsonObject = JSONObject.parseObject(search.toString());
                if (id.equals(jsonObject.get("id"))){
                    return JSONObject.toJavaObject(jsonObject,SearchData.class);
                }
            }
        }
        return null;
    }
    public void popSearch(List<SearchData> datas){
        for (SearchData data: datas) {
            redisTemplate.opsForSet().remove("searchs" ,JSONObject.toJSONString(data));
        }
    }
    public void popSearch(SearchData data){
        redisTemplate.opsForSet().remove("searchs" ,JSONObject.toJSONString(data));
    }
    public void pushCode(SmsCode smsCode){
        SmsCode object = findCode(smsCode.getId());
        if (object != null){
            popCode(object);
        }
//        System.out.println(smsCode);
//        redisTemplate.opsForSet().add("smsCode",JSONObject.toJSONString(smsCode),5, TimeUnit.MINUTES);
        redisTemplate.opsForSet().add("smsCode",JSONObject.toJSONString(smsCode));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                popCode(smsCode);
            }
        }, 1000 * 60 * 60);
    }
    public SmsCode findCode(String id){
        Set smsCode = redisTemplate.opsForSet().members("smsCode");
        if (smsCode != null){
            JSONObject jsonObject = new JSONObject();
            for (Object code: smsCode) {
//                System.out.println(code);
                jsonObject = JSONObject.parseObject(code.toString());
                if (id.equals(jsonObject.get("id"))){
                    return JSONObject.toJavaObject(jsonObject,SmsCode.class);
                }
            }
        }
        return null;
    }
    public void removeByPhone(String phone){
        Set smsCode = redisTemplate.opsForSet().members("smsCode");
        if (smsCode != null){
            JSONObject jsonObject = new JSONObject();
            for (Object code: smsCode) {
                jsonObject = JSONObject.parseObject(code.toString());
                if (phone.equals(jsonObject.get("phone"))){
                    popCode(JSONObject.toJavaObject(jsonObject,SmsCode.class));
                }
            }
        }
    }
    public SmsCode findByPhone(String phone){
        Set smsCode = redisTemplate.opsForSet().members("smsCode");
        if (smsCode != null){
            JSONObject jsonObject = new JSONObject();
            for (Object code: smsCode) {
                jsonObject = JSONObject.parseObject(code.toString());
                if (phone.equals(jsonObject.get("phone"))){
                    return (JSONObject.toJavaObject(jsonObject,SmsCode.class));
                }
            }
        }
        return null;
    }
    public void popCode(SmsCode code){
        redisTemplate.opsForSet().remove("smsCode" ,JSONObject.toJSONString(code));
    }
    public void pushUser(SysUser userToken){
        if (StringUtils.isNotEmpty(userToken.getToken())) {
            Set users = redisTemplate.opsForSet().members("sysUsers");
            assert users != null;
//        System.out.println(users.toString());
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                SysUser userEntity = objectMapper.convertValue(user, SysUser.class);
                if (userEntity.getToken().equals(userToken.getToken()) || (userEntity.getId() > 0 && userToken.getId() > 0 && userEntity.getId() == userToken.getId())){
                    popUser(userEntity);
                }
            }
            redisTemplate.opsForSet().add("sysUsers",userToken);
        }
    }
    public void popUser(SysUser userToken){
        redisTemplate.opsForSet().remove("sysUsers" ,userToken);
    }
    public SysUser findSysUserByToken(String token) {
        Set users = redisTemplate.opsForSet().members("sysUsers");
//        System.out.println(users);
        if (users == null) return null;
        for (Object user: users) {
//            System.out.println(user);
            ObjectMapper objectMapper = new ObjectMapper();
            if (user != null){
                SysUser userEntity = objectMapper.convertValue(user,SysUser.class);
                if (userEntity != null && token.equals(userEntity.getToken())){
                    return userEntity;
                }
            }
        }
        return null;
    }
    public void pushUser(User userToken){
        if (StringUtils.isNotEmpty(userToken.getToken())) {
            Set users = redisTemplate.opsForSet().members("Users");
            assert users != null;
//        System.out.println(users.toString());
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user, User.class);
                if (userEntity.getToken().equals(userToken.getToken()) || (userEntity.getId() > 0 && userToken.getId() > 0 && userEntity.getId() == userToken.getId())){
                    popUser(userEntity);
                }
            }
            redisTemplate.opsForSet().add("Users",userToken);
        }
    }
    public void updateUser(String token){
        User user = findUserByToken(token);
        if (user != null){
            if (user.getId() > 0){
                user = userDao.findAllById(user.getId());
                if (user != null){
                    user.setToken(token);
                    pushUser(user);
                }
            }else {
                pushUser(user);
            }
        }
    }
    public void removeUser(User userToken){
        Set users = redisTemplate.opsForSet().members("Users");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (userEntity.getToken().equals(userToken.getToken())){
                    popUser(userEntity);
                }
            }
        }
    }
    public void popUser(User userToken){
        redisTemplate.opsForSet().remove("Users" ,userToken);
    }
    public User findUserByToken(String token) {
        Set users = redisTemplate.opsForSet().members("Users");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (userEntity.getToken().equals(token)){
                    return userEntity;
                }
            }
        }
        return null;
    }

    public User findUserByUserId(long userId) {
        Set users = redisTemplate.opsForSet().members("Users");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (userEntity.getId()== userId){
                    return userEntity;
                }
            }
        }
        return null;
    }

    public Set getAllUser(){
        return redisTemplate.opsForSet().members("Users");
    }
}
