package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.SmsCode;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.TimeUtil;
import com.example.movie2admin.util.ToolsUtil;
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
    @Autowired
    private UserConfigDao userConfigDao;
    @Autowired
    private UserConsumeDao userConsumeDao;
    @Autowired
    private UserSpreadRebateDao userSpreadRebateDao;
    @Autowired
    private AgentRebateDao agentRebateDao;
    @Autowired
    private UserSpreadRecordDao userSpreadRecordDao;

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
            User u = authDao.findUserByUserId(users.getId());
            authDao.popUser(u);
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
            User u = authDao.findUserByUserId(users.getId());
            authDao.popUser(u);
            array.add(getUsers(users));
        }
        return ResponseData.success(array);
    }
    public ResponseData getConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<UserConfig> configs = userConfigDao.findAll();
        JSONObject object = new JSONObject();
        for (UserConfig config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData updateConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        userConfigDao.saveAllAndFlush(getUpdateConfig(data));
        return ResponseData.success();
    }
    public List<UserConfig> getUpdateConfig(JSONObject data){
        List<UserConfig> configs = new ArrayList<>();
        JSONObject object = new JSONObject();
        for (String key : data.keySet()) {
            if (
                    data.get(key) != null && !key.equals("ip") &&
                            data.get(key) != null && !key.equals("isWeb") &&
                            data.get(key) != null && !key.equals("serverName") &&
                            data.get(key) != null && !key.equals("serverPort") &&
                            data.get(key) != null && !key.equals("uri") &&
                            data.get(key) != null && !key.equals("url") &&
                            data.get(key) != null && !key.equals("schema") &&
                            data.get(key) != null && !key.equals("user")
            ){
                object.put(key, data.get(key));
            }
        }
        for (String key : object.keySet()) {
            UserConfig config = userConfigDao.findByName(key);
            if (config == null){
                config = new UserConfig(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }
    public ResponseData getUsersConsumeList(String title, int type, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<User> userPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(title)) {
            if (type == 0){
                userPage = userDao.findAllById(new Long(title),pageable);
            }else if (type == 1){
                userPage = userDao.findAllByNickname("%"+title+"%",pageable);
            }else if (type == 2){
                userPage = userDao.findAllByUsername("%"+title+"%",pageable);
            }else if (type == 3){
                userPage = userDao.findAllByPhone("%"+title+"%",pageable);
            }else {
                userPage = userDao.findAll(pageable);
            }
        }else {
            userPage = userDao.findAll(pageable);
        }
//        System.out.println(userPage.getContent());
        JSONArray array = new JSONArray();
        for (User profile : userPage.getContent()) {
            JSONObject object = new JSONObject();
            object.put("id",profile.getId());
            object.put("nickname",profile.getNickname());
            object.put("username",profile.getUsername());
            object.put("phone",profile.getPhone());
            object.put("addTime",profile.getAddTime());
            Double all = 0D;
            Double agent = 0D;
            Double users = 0D;
            if (start > 0 && end > 0) {
                all = userConsumeDao.getAllByUserId(profile.getId(),start,end);
                agent = userConsumeDao.getAllByAgent(profile.getId(),start,end);
                users = userConsumeDao.getAllByUsers(profile.getId(),start,end);
            } else if (start > 0){
                all = userConsumeDao.getAllByUserId(profile.getId(),start);
                agent = userConsumeDao.getAllByAgent(profile.getId(),start);
                users = userConsumeDao.getAllByUsers(profile.getId(),start);
            } else if (end > 0){
                all = userConsumeDao.getAllByUserIdEnd(profile.getId(),end);
                agent = userConsumeDao.getAllByAgentEnd(profile.getId(),end);
                users = userConsumeDao.getAllByUsersEnd(profile.getId(),end);
            }else{
                all = userConsumeDao.getAllByUserId(profile.getId());
                agent = userConsumeDao.getAllByAgent(profile.getId());
                users = userConsumeDao.getAllByUsers(profile.getId());
            }
            object.put("agent", String.format("%.2f", agent));
            object.put("users", String.format("%.2f", users));
            object.put("all", String.format("%.2f", all));
            object.put("records", userConsumeDao.countAllByUserId(profile.getId()));
            array.add(object);
        }
        JSONObject josn = ResponseData.object("total", userPage.getTotalElements());
        josn.put("list", array);
        return ResponseData.success(josn);
    }
    public ResponseData getUsersConsumeListUser(long userId, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        User u = userDao.findAllById(userId);
        if (u == null) return ResponseData.error("用户不存在!");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<UserConsume> userPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();

        if (start > 0 && end > 0) {
//            System.out.println(start);
//            System.out.println(end);
            userPage = userConsumeDao.getUsersConsumeListUser(userId,start,end,pageable);
        } else if (start > 0){
//            System.out.println(start);
            userPage = userConsumeDao.getUsersConsumeListUser(userId, start,pageable);
        } else if (end > 0){
            userPage = userConsumeDao.getUsersConsumeListUserEnd(userId, end,pageable);
        }else{
            userPage = userConsumeDao.getUsersConsumeListUser(userId,pageable);
        }
        JSONArray array = new JSONArray();
        for (UserConsume consume : userPage.getContent()) {
            array.add(getUserConsume(consume));
        }
        JSONObject object = ResponseData.object("total", userPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getUserConsume(UserConsume consume){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(consume));
        Double users = userSpreadRebateDao.getAllByOrderId(consume.getId());
        Double agent = agentRebateDao.getAllByOrderId(consume.getId());
        json.put("users", String.format("%.2f", users));
        json.put("agent", String.format("%.2f", agent));
        return json;
    }

    public ResponseData makeupConsumeListUser(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<UserConsume> consumeList = userConsumeDao.findAllById(ids);
        for (int i = 0; i < consumeList.size(); i++) {
            consumeList.get(i).setStatus(1);
            consumeList.get(i).setUpdateTime(System.currentTimeMillis());
            agentRebateDao.setOrderByUsers(1,consumeList.get(i).getId(),System.currentTimeMillis());
            userConsumeDao.makeupOrderByUsers(consumeList.get(i).getId(),System.currentTimeMillis());
        }
        userConsumeDao.saveAllAndFlush(consumeList);
        JSONArray array = new JSONArray();
        for (UserConsume consume : consumeList) {
            array.add(getUserConsume(consume));
        }
        return ResponseData.success(array);
    }

    public ResponseData makeDownConsumeListUser(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<UserConsume> consumeList = userConsumeDao.findAllById(ids);
        for (int i = 0; i < consumeList.size(); i++) {
            consumeList.get(i).setStatus(-1);
            consumeList.get(i).setUpdateTime(System.currentTimeMillis());
            agentRebateDao.setOrderByUsers(-1,consumeList.get(i).getId(),System.currentTimeMillis());
//            userConsumeDao.makeDownOrderByUsers(consumeList.get(i).getId(),System.currentTimeMillis());
            UserSpreadRecord record = userSpreadRecordDao.findAllByUserId(consumeList.get(i).getUserId());
            if (record == null) {
                userConsumeDao.makeDownOrderByUsers(consumeList.get(i).getId(),System.currentTimeMillis());
            }else{
                userConsumeDao.makeDownOrderByUsers(consumeList.get(i).getId(), record.getShareUserId(),System.currentTimeMillis());
            }
        }
        userConsumeDao.saveAllAndFlush(consumeList);
        JSONArray array = new JSONArray();
        for (UserConsume consume : consumeList) {
            array.add(getUserConsume(consume));
        }
        return ResponseData.success(array);
    }

    public ResponseData getUsersSpreadRecordList(String title, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit);
        Page<UserSpreadRecord> recordPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(title)) {
            if (ToolsUtil.isNumberString(title)){
                recordPage = userDao.getUsersSpreadRecordList(new Long(title),pageable);
            }else{
                recordPage = userDao.getUsersSpreadRecordList("%"+title+"%",pageable);
            }
        }else {
            recordPage = userDao.getUsersSpreadRecordList(pageable);
        }
//        System.out.println(userPage.getContent());
        JSONArray array = new JSONArray();
        for (UserSpreadRecord record : recordPage.getContent()) {
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(record));
            object.put("user", "无");
            User profile = userDao.findAllById(record.getShareUserId());
            record = userSpreadRecordDao.findAllByUserId(profile.getId());
            if (record!= null) {
                User u = userDao.findAllById(record.getShareUserId());
                object.put("user", u.getNickname());
            }
            Double rebate = userSpreadRebateDao.getUsersSpreadRecordList(profile.getId());
            object.put("nickname",profile.getNickname());
            object.put("username",profile.getUsername());
            object.put("phone",profile.getPhone());
            object.put("registerTime",profile.getAddTime());
            object.put("registerIP",profile.getRegisterIp());
            object.put("records", userSpreadRecordDao.countAllByShareUserId(profile.getId()));
            object.put("rebate", String.format("%.2f", rebate));
            array.add(object);
        }
        JSONObject josn = ResponseData.object("total", recordPage.getTotalElements());
        josn.put("list", array);
        return ResponseData.success(josn);
    }

    public ResponseData getUsersSpreadRecordUserList(long userId, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        User u = userDao.findAllById(userId);
        if (u == null) return ResponseData.error("用户不存在!");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<UserSpreadRecord> recordPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();

        if (start > 0 && end > 0) {
            recordPage = userSpreadRecordDao.getUsersSpreadRecordUserList(userId,start,end,pageable);
        } else if (start > 0){
            recordPage = userSpreadRecordDao.getUsersSpreadRecordUserList(userId, start,pageable);
        } else if (end > 0){
            recordPage = userSpreadRecordDao.getUsersSpreadRecordUserListEnd(userId, end,pageable);
        }else{
            recordPage = userSpreadRecordDao.getUsersSpreadRecordUserList(userId,pageable);
        }
        JSONArray array = new JSONArray();
        for (UserSpreadRecord record : recordPage.getContent()) {
            array.add(getUserSpreadRecord(record));
        }
        JSONObject object = ResponseData.object("total", recordPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    private Object getUserSpreadRecord(UserSpreadRecord record) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(record));
        User profile = userDao.findAllById(record.getShareUserId());
        Double rebate = userSpreadRebateDao.getUserSpreadRecord(record.getShareUserId(),record.getUserId());
        Double consume = userConsumeDao.getUserSpreadRecord(record.getUserId());
        object.put("nickname",profile.getNickname());
        object.put("username",profile.getUsername());
        object.put("phone",profile.getPhone());
        object.put("record",userSpreadRecordDao.countAllByShareUserId(record.getUserId()));
        object.put("rebate", String.format("%.2f", rebate));
        object.put("consume", String.format("%.2f", consume));
        return object;
    }

    public ResponseData deleteUsersSpreadRecordUserList(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        userSpreadRecordDao.deleteAllById(ids);
        return ResponseData.success();
    }
}
