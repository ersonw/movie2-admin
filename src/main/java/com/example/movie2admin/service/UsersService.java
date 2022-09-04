package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.SmsCode;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UsersService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private MembershipExperienceDao membershipExperienceDao;
    @Autowired
    private MembershipLevelDao membershipLevelDao;
    @Autowired
    private SmsRecordDao smsRecordDao;
    @Autowired
    private SmsBaoService smsBaoService;
    @Autowired
    private UserFailLoginRecordDao failLoginRecordDao;
    @Autowired
    private MembershipExpiredDao membershipExpiredDao;
    @Autowired
    private UserFollowDao userFollowDao;

    private static long FAIL_LOGIN_TIMES = 6;

    public boolean isMembership(long userId) {
        return getMember(userId);
    }
    private long checkFailLogin(long userId){
        List<UserFailLoginRecord> records = failLoginRecordDao.checkUserToday(userId, TimeUtil.getTodayZero());
        return FAIL_LOGIN_TIMES - records.size();
    }
    public JSONObject getUserInfo(User user){
        JSONObject object = ResponseData.object("token", user.getToken());
        object.put("id",user.getId());
        object.put("avatar",user.getAvatar());
        object.put("nickname",user.getNickname());
        object.put("text",user.getText());
        object.put("username",user.getUsername());
        object.put("expired",getExpired(user.getId()));
//        object.put("phone",user.getPhone());
        String phone = user.getPhone();
        object.put("phone", phone.substring(0,4) + "****" + phone.substring(phone.length() - 4));
        object.put("email",user.getEmail());
        object.put("member",getMember(user.getId()));
        object.put("level", getMemberLevel(user.getId()));
        return object;
    }
    public long getExpired(long userId){
        MembershipExpired expired = membershipExpiredDao.findAllByUserId(userId);
        if(expired!=null){
            return expired.getExpired()+expired.getAddTime();
        }
        return 0;
    }
    public boolean getMember(long userId){
        return getExpired(userId) > System.currentTimeMillis();
    }
    public long getMemberLevel(long userId){
        long level =0;
        long experience = membershipExperienceDao.getAllByUserId(userId);
        long experienced = 0;
        while (experience > experienced){
            level++;
            MembershipLevel l = membershipLevelDao.findByLevel(level);
            if (l==null){
                break;
            }
            experienced = l.getExperience();
            experience -= l.getExperience();
        }
        if(experience < 0){
            level--;
        }
        return level;
    }
    public long getExperience(long userId){
        long level =0;
        long experience = membershipExperienceDao.getAllByUserId(userId);
        long experienced = 0;
        while (experience > experienced){
//            log.info("experienced: {} experience:{}",experienced,experience);
            level++;
            MembershipLevel l = membershipLevelDao.findByLevel(level);
            if (l==null){
                break;
            }
            experienced = l.getExperience();
            experience -= l.getExperience();
        }
        if(experience < 0){
            experience = experienced+experience;
        }
        return experience;
    }
    public long getExperienced(long userId){
        MembershipLevel grade = membershipLevelDao.findByLevel(getMemberLevel(userId)+1);
        return grade.getExperience();
    }
    public long checkSmsLast(String phone){
        SmsRecord record = smsRecordDao.getLast(phone);
        if (record == null){
            return 0;
        }
        long last = System.currentTimeMillis() - record.getAddTime();
        long ms = 1000 * 60 * 2;
        if (last > ms){
            return 0;
        }
        return (ms - last) / 1000;
    }
    public boolean checkSmsMax(String phone){
        long count = smsRecordDao.countTodayMax(TimeUtil.getTodayZero(),phone);
        long max = Long.parseLong(smsBaoService.getValueByKey("smsCountMaxDay"));
        return count < max;
    }
    public String verifyCode(String id, String code){
        SmsCode smsCode = authDao.findCode(id);
        if (smsCode != null && smsCode.getCode().equals(code)){
            authDao.popCode(smsCode);
//            SmsRecords smsRecords = smsRecordsDao.findAllByData(smsCode.getId());
//            smsRecords.setStatus(2);
//            smsRecordsDao.saveAndFlush(smsRecords);
            return smsCode.getPhone();
        }
        return null;
    }
    public long getProgressProfile(User user){
        long progress = 0;
        if(StringUtils.isNotEmpty(user.getNickname())) progress+=10;
        if(StringUtils.isNotEmpty(user.getAvatar())) progress+=10;
        if(StringUtils.isNotEmpty(user.getText())) progress+=10;
        if(StringUtils.isNotEmpty(user.getUsername())) progress+=10;
        if(StringUtils.isNotEmpty(user.getPhone())) progress+=10;
        if(StringUtils.isNotEmpty(user.getEmail())) progress+=10;
        return progress;
    }
    public JSONObject getUserFans(UserFollow userFollow, long userId){
        JSONObject object = new JSONObject();
        User user = userDao.findAllById(userFollow.getUserId());
        if (user == null) return object;
        object.put("id", user.getId());
        object.put("avatar", user.getAvatar());
        object.put("nickname", user.getNickname());
        object.put("fans", userFollowDao.countAllByToUserId(user.getId()));
        object.put("followed", userFollowDao.findAllByUserIdAndToUserId(user.getId(),userId) != null);
        object.put("member",getMember(user.getId()));
        object.put("level", getMemberLevel(user.getId()));
        if(user.getId() == userId){
            object.put("follow", true);
        }else {
            object.put("follow", userFollowDao.findAllByUserIdAndToUserId(userId, user.getId()) != null);
        }
        return object;
    }
    public JSONObject getUserFollow(UserFollow userFollow, long userId){
        JSONObject object = new JSONObject();
        User user = userDao.findAllById(userFollow.getToUserId());
        if (user == null) return object;
        object.put("id", user.getId());
        object.put("avatar", user.getAvatar());
        object.put("nickname", user.getNickname());
        object.put("fans", userFollowDao.countAllByToUserId(user.getId()));
        object.put("followed", userFollowDao.findAllByUserIdAndToUserId(user.getId(),userId) != null);
        object.put("member",getMember(user.getId()));
        object.put("level", getMemberLevel(user.getId()));
        if(user.getId() == userId){
            object.put("follow", true);
        }else {
            object.put("follow", userFollowDao.findAllByUserIdAndToUserId(userId, user.getId()) != null);
        }
        return object;
    }
}
