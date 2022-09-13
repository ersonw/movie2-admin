package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.SmsCode;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.TimeUtil;
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
    @Autowired
    private GameOrderDao gameOrderDao;
    @Autowired
    private MembershipOrderDao membershipOrderDao;
    @Autowired
    private DiamondOrderDao diamondOrderDao;
    @Autowired
    private CoinOrderDao coinOrderdao;
    @Autowired
    private CashOrderDao cashOrderDao;
    @Autowired
    private ShortVideoDao shortVideoDao;
    @Autowired
    private VideoPayRecordDao videoPayRecordDao;
    @Autowired
    private GameOutOrderDao gameOutOrderDao;

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
    public int getMemberLevel(long userId){
        int level =0;
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
        int level =0;
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

    public ResponseData getUserList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<User> userPage;
        if (StringUtils.isNotEmpty(title)) {
            userPage = userDao.findAllByUsernameOrNicknameOrPhone("%"+title+"%","%"+title+"%","%"+title+"%",pageable);
        }else {
            userPage = userDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (User users : userPage.getContent()) {
            array.add(getUsers(users));
        }
        JSONObject object = ResponseData.object("total", userPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    private Object getUsers(User users) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(users));
        double in = gameOrderDao.getAllByUserId(users.getId());
        in += membershipOrderDao.getAllByUserId(users.getId());
        in += coinOrderdao.getAllByUserId(users.getId());
        in += diamondOrderDao.getAllByUserId(users.getId());
        in += cashOrderDao.getAllByUserId(users.getId());
        double gameOut = gameOutOrderDao.getAllByUserId(users.getId());

        object.put("in", String.format("%.2f", in * 1D));
        object.put("gameOut", String.format("%.2f", gameOut * 1D));
        object.put("pays", videoPayRecordDao.countAllByUserId(users.getId()));
        object.put("short", shortVideoDao.countAllByUserId(users.getId()));
        return object;
    }

    public ResponseData deleteUser(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        return ResponseData.error("暂未开发!");
    }

    public ResponseData freezeUser(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<User> userList = userDao.findAllById(ids);
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setStatus(-1);
        }
        userDao.saveAllAndFlush(userList);
        JSONArray array = new JSONArray();
        for (User users : userList) {
            authDao.popUser(users);
            array.add(getUsers(users));
        }
        return ResponseData.success(array);
    }

    public ResponseData unfreezeUser(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<User> userList = userDao.findAllById(ids);
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setStatus(1);
        }
        userDao.saveAllAndFlush(userList);
        JSONArray array = new JSONArray();
        for (User users : userList) {
            authDao.popUser(users);
            array.add(getUsers(users));
        }
        return ResponseData.success(array);
    }
}
