package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.WaLiUtil;
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
    @Autowired
    private CashInConfigDao cashInConfigDao;
    @Autowired
    private GameFundsDao gameFundsDao;
    @Autowired
    private UserConsumeDao userConsumeDao;

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
    public JSONObject getBenefit(MembershipBenefit benefit){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(benefit));
        return object;
    }
    public ResponseData getBenefitList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<MembershipBenefit> benefitPage;
        if (StringUtils.isNotEmpty(title)) {
            benefitPage = membershipBenefitDao.findAllByNameLike("%"+title+"%",pageable);
        }else {
            benefitPage = membershipBenefitDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipBenefit benefit : benefitPage.getContent()) {
            array.add(getBenefit(benefit));
        }
        JSONObject object = ResponseData.object("total", benefitPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteBenefit(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipBenefitDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateBenefit(long id, String name, String icon, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 0) return ResponseData.error("记录不存在!");
        MembershipBenefit benefit = membershipBenefitDao.findAllById(id);
        if (benefit == null) return ResponseData.error("记录不存在");
        name = name.replaceAll(" ", "").trim().toUpperCase();
        benefit.setName(name);
        benefit.setIcon(icon);
        benefit.setUpdateTime(System.currentTimeMillis());
        if (benefit.getAddTime() == 0)benefit.setAddTime(System.currentTimeMillis());
        membershipBenefitDao.save(benefit);
        return ResponseData.success(getBenefit(benefit));
    }

    public ResponseData addBenefit(String name, String icon, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        name = name.replaceAll(" ", "").trim().toUpperCase();
        MembershipBenefit benefit = membershipBenefitDao.findAllByName(name);
        if (benefit != null) return ResponseData.error("记录已存在");
        benefit = new MembershipBenefit();
        benefit.setName(name);
        benefit.setIcon(icon);
        benefit.setAddTime(System.currentTimeMillis());
        benefit.setUpdateTime(System.currentTimeMillis());
        membershipBenefitDao.save(benefit);
        return ResponseData.success(getBenefit(benefit));
    }

    public ResponseData getGradeList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC,"mini"));
        Page<MembershipGrade> gradePage;
        if (StringUtils.isNotEmpty(title)) {
            gradePage = membershipGradeDao.findAllByNameLike("%"+title+"%",pageable);
        }else {
            gradePage = membershipGradeDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipGrade grade : gradePage.getContent()) {
            array.add(getGrade(grade));
        }
        JSONObject object = ResponseData.object("total", gradePage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    private JSONObject getGrade(MembershipGrade grade) {
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(grade));
        if (StringUtils.isNotEmpty(grade.getBenefit())){
            String[] benefits = grade.getBenefit().split("-");
            StringBuilder sb = new StringBuilder();
            List<Long> options = new ArrayList<>();
            for (String b: benefits) {
                MembershipBenefit benefit = membershipBenefitDao.findAllById(new Long(b));
                if (benefit == null) continue;
                options.add(new Long(b));
                sb.append(benefit.getName()).append(",");
            }
            object.put("benefits", sb.toString());
            object.put("options", options);
        }
        return object;
    }

    public ResponseData deleteGrade(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipGradeDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateGrade(long id, String name, String icon, int mini, int max, List<Long> options,int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 0) return ResponseData.error("记录不存在!");
        MembershipGrade grade = membershipGradeDao.findAllById(id);
        if (grade == null) return ResponseData.error("记录不存在");
        if (StringUtils.isEmpty(name)) return ResponseData.error("权益名不能为空");
        if (StringUtils.isEmpty(icon)) return ResponseData.error("背景图片不能为空");
        name = name.replaceAll(" ", "").trim().toUpperCase();
        grade.setName(name);
        grade.setIcon(icon);
        grade.setMini(mini);
        grade.setMax(max);
        grade.setBenefit(getBenefit(options));
        grade.setStatus(status);
        grade.setUpdateTime(System.currentTimeMillis());
        if (grade.getAddTime() == 0) grade.setAddTime(System.currentTimeMillis());
        membershipGradeDao.save(grade);
        return ResponseData.success(getGrade(grade));
    }
    public String getBenefit(List<Long> options){
        StringBuilder benefit = new StringBuilder();
        for (Long option : options) {
            MembershipBenefit b = membershipBenefitDao.findAllById(option);
            if (b == null) continue;
            benefit.append(b.getId()).append("-");
        }
        return benefit.toString();
    }
    public ResponseData addGrade(String name, String icon, int mini, int max, List<Long> options, int status,SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(name)) return ResponseData.error("权益名不能为空");
        if (StringUtils.isEmpty(icon)) return ResponseData.error("背景图片不能为空");
        name = name.replaceAll(" ", "").trim().toUpperCase();
        MembershipGrade grade = membershipGradeDao.findAllByName(name);
        if (grade != null) return ResponseData.error("记录已存在");
        grade = new MembershipGrade();
        grade.setName(name);
        grade.setIcon(icon);
        grade.setMini(mini);
        grade.setMax(max);
        grade.setBenefit(getBenefit(options));
        grade.setStatus(status);
        grade.setAddTime(System.currentTimeMillis());
        grade.setUpdateTime(System.currentTimeMillis());
        membershipGradeDao.save(grade);
        return ResponseData.success(getGrade(grade));
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
    public JSONObject getButton(MembershipButton button){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(button));
        json.put("cashIn","不指定充值通道");
        json.put("typeStr","无支付方式");
        List<CashInOption> options = new ArrayList<>();
        if (button.getCashInId() > 0){
            json.put("cashIn","指定充值通道已被删除");
            CashInConfig config = cashInConfigDao.findAllById(button.getCashInId());
            if (config != null){
                json.put("cashIn", config.getTitle());
                options = getAllowed(config);
            }
        }else{
            options = cashInOptionDao.findAll();
        }
        if (options.size() > 0){
            StringBuilder sb = new StringBuilder();
            for (CashInOption option: options) {
                sb.append(option.getName()).append(",");
            }
            json.put("typeStr",sb.toString());
        }
        return json;
    }
    public ResponseData getButtonList(long title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC,"amount"));
        Page<MembershipButton> buttonPage;
        if (title > 0) {
            buttonPage = membershipButtonDao.findAllByAmountGreaterThanEqual(title,pageable);
        }else {
            buttonPage = membershipButtonDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipButton button : buttonPage.getContent()) {
            array.add(getButton(button));
        }
        JSONObject object = ResponseData.object("total", buttonPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteButton(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<MembershipButton> buttons = membershipButtonDao.findAllById(ids);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setStatus(-1);
        }
        membershipButtonDao.saveAllAndFlush(buttons);
        return ResponseData.success();
    }

    public ResponseData updateButton(long id, long amount, Double price, long original, long gameCoin, long experience, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        MembershipButton button = membershipButtonDao.findAllById(id);
        if (button == null) return ResponseData.error("按钮不存在");
        if (cashInId > 0){
            CashInConfig config = cashInConfigDao.findAllById(cashInId);
            if (config == null) return ResponseData.error("通道ID不存在");
        }else{
            cashInId = 0;
        }
        button.setAmount(amount);
        button.setPrice(price.longValue());
        button.setOriginal(original);
        button.setGameCoin(gameCoin);
        button.setExperience(experience);
        button.setCashInId(cashInId);
        button.setStatus(status);
        button.setUpdateTime(System.currentTimeMillis());
        if (button.getAddTime() == 0) button.setAddTime(System.currentTimeMillis());
        membershipButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }
    public ResponseData addButton(long amount, Double price, long original, long gameCoin, long experience, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (cashInId > 0){
            CashInConfig config = cashInConfigDao.findAllById(cashInId);
            if (config == null) return ResponseData.error("通道ID不存在");
        }else{
            cashInId = 0;
        }
        MembershipButton button = new MembershipButton();
        button.setAmount(amount);
        button.setPrice(price.longValue());
        button.setOriginal(original);
        button.setGameCoin(gameCoin);
        button.setExperience(experience);
        button.setCashInId(cashInId);
        button.setStatus(status);
        button.setUpdateTime(System.currentTimeMillis());
        button.setAddTime(System.currentTimeMillis());
        membershipButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }
    public ResponseData getOrderList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<MembershipOrder> orderPage;
        if (StringUtils.isNotEmpty(title)) {
            orderPage = membershipOrderDao.findAllByOrderNoLike("%"+title+"%",pageable);
        }else {
            orderPage = membershipOrderDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipOrder order : orderPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(order));
            User u = userDao.findAllById(order.getUserId());
            CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(order.getOrderNo());
            json.put("price", String.format("%.2f", order.getPrice() / 100D));
            json.put("user", "用户不存在");
            json.put("status",0);
            json.put("type","支付方式不存在");
            json.put("ip","没有IP");
            if (u!= null) {
                json.put("user", u.getUsername());
            }
            if (inOrder!= null && inOrder.getStatus() >= 0) {
                json.put("status",inOrder.getStatus());
                json.put("totalFee",inOrder.getTotalFee());
                json.put("tradeNo",inOrder.getTradeNo());
                CashInOption option = cashInOptionDao.findAllById(inOrder.getType());
                if (option != null)json.put("type", option.getName());
                json.put("ip",inOrder.getIp());
                array.add(json);
            }
        }
        JSONObject object = ResponseData.object("total", orderPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<MembershipOrder> orders = membershipOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            CashInOrder order = cashInOrderDao.findAllByOrderNo(orders.get(i).getOrderNo());
            if (order != null){
                order.setStatus(-1);
                inOrders.add(order);
            }
        }
        membershipOrderDao.saveAllAndFlush(orders);
        cashInOrderDao.saveAllAndFlush(inOrders);
        return ResponseData.success();
    }
    public ResponseData makeupOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<MembershipOrder> orders = membershipOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (MembershipOrder order: orders) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(order));
            User u = userDao.findAllById(order.getUserId());
            CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(order.getOrderNo());
            json.put("price", String.format("%.2f", order.getPrice() / 100D));
            json.put("user", "用户不存在");
            json.put("status",0);
            json.put("type","支付方式不存在");
            json.put("ip","没有IP");
            if (u!= null) {
                json.put("user", u.getUsername());
            }
            if (inOrder != null && inOrder.getStatus() != 1) {
                inOrder.setStatus(1);
                inOrders.add(inOrder);
                json.put("status",inOrder.getStatus());
                json.put("totalFee",inOrder.getTotalFee());
                json.put("tradeNo",inOrder.getTradeNo());
                CashInOption option = cashInOptionDao.findAllById(inOrder.getType());
                if (option != null)json.put("type", option.getName());
                json.put("ip",inOrder.getIp());
                array.add(json);
            }
        }
        cashInOrderDao.saveAllAndFlush(inOrders);
        for (CashInOrder order: inOrders) {
            handlerOrder(order.getOrderNo());
        }
        return ResponseData.success(array);
    }
    public boolean handlerOrder(String orderId){
        MembershipOrder order = membershipOrderDao.findAllByOrderNo(orderId);
        CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(orderId);
        if (order == null || inOrder == null) return false;
        User user = userDao.findAllById(order.getUserId());
        if (user == null) return false;
        MembershipFunds balance = new MembershipFunds();
        balance.setAddTime(System.currentTimeMillis());
        balance.setAmount(order.getAmount());
        balance.setGameCoin(order.getGameCoin());
        balance.setExperience(order.getExperience());
        balance.setUserId(user.getId());
        balance.setText("在线开通-后台补单");
        MembershipExpired expired = membershipExpiredDao.findAllByUserId(user.getId());
        long time = order.getAmount() * 24 * 60 * 60 * 1000;
        if(expired == null ){
            MembershipLevel grade = membershipLevelDao.findByLevel(1);
            membershipExperienceDao.save(new MembershipExperience(user.getId(), "首次开通赠送", grade.getExperience()));
            expired= new MembershipExpired();
            expired.setAddTime(System.currentTimeMillis());
            expired.setUserId(user.getId());
            expired.setExpired(0);
        }
        expired.setUpdateTime(System.currentTimeMillis());
        if ((expired.getExpired() + expired.getAddTime()) > System.currentTimeMillis()){
            expired.setExpired(expired.getExpired()+time);
        }else{
            expired.setExpired((System.currentTimeMillis() - expired.getAddTime())+time);
        }
        membershipExpiredDao.save(expired);
        membershipFundsDao.save(balance);
        if (order.getExperience() > 0) {
            membershipExperienceDao.save(new MembershipExperience(user.getId(), "开通会员赠送", order.getExperience()));
        }
        if (order.getGameCoin() > 0) {
            if(WaLiUtil.tranferV3(user.getId(), order.getGameCoin()* 100)){
                gameFundsDao.save(new GameFunds(user.getId(), order.getGameCoin()* 100, "开通会员赠送"));
            }else {
                log.info("会员充值赠送失败 会员ID：{} 赠送金额：{}",user.getId(), order.getGameCoin());
            }
        }
        if (StringUtils.isEmpty(inOrder.getTotalFee())) inOrder.setTotalFee("0.0");
        UserConsume consume = new UserConsume(user.getId(), new Double(inOrder.getTotalFee()).longValue(),"在线开通-后台补单:会员"+order.getAmount()+"天",1);
        userConsumeDao.saveAndFlush(consume);
        return true;
    }

    public ResponseData getLevelList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC,"level"));
        Page<MembershipLevel> levelPage;
        if (StringUtils.isNotEmpty(title)) {
            levelPage = membershipLevelDao.findAllByLevel(new Integer(title),pageable);
        }else {
            levelPage = membershipLevelDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (MembershipLevel level : levelPage.getContent()) {
            array.add(getLevel(level));
        }
        JSONObject object = ResponseData.object("total", levelPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getLevel(MembershipLevel level){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(level));
        return object;
    }

    public ResponseData deleteLevel(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        membershipLevelDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateLevel(long id, int level, long experience, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        MembershipLevel levelObj = membershipLevelDao.findAllById(id);
        if (levelObj == null) return ResponseData.error("记录不存在");
        MembershipLevel Obj = membershipLevelDao.findAllByLevel(level);
        if (Obj != null && Obj.getId() != id) return ResponseData.error("等级已存在");
        levelObj.setLevel(level);
        levelObj.setExperience(experience);
        if (levelObj.getAddTime() == 0)levelObj.setAddTime(System.currentTimeMillis());
        levelObj.setUpdateTime(System.currentTimeMillis());
        membershipLevelDao.save(levelObj);
        return ResponseData.success(getLevel(levelObj));
    }

    public ResponseData addLevel(int level, long experience, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (level < 1) return ResponseData.error("等级不可小于1");
        MembershipLevel levelObj = membershipLevelDao.findAllByLevel(level);
        if (levelObj != null) return ResponseData.error("等级已存在");
        levelObj = new MembershipLevel();
        levelObj.setLevel(level);
        levelObj.setExperience(experience);
        levelObj.setAddTime(System.currentTimeMillis());
        levelObj.setUpdateTime(System.currentTimeMillis());
        membershipLevelDao.save(levelObj);
        return ResponseData.success(getLevel(levelObj));
    }

    public ResponseData getConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<MembershipConfig> configs = membershipConfigDao.findAll();
        JSONObject object = new JSONObject();
        for (MembershipConfig config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData updateConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        membershipConfigDao.saveAllAndFlush(getDiamondUpdateConfig(data));
        return ResponseData.success();
    }
    public List<MembershipConfig> getDiamondUpdateConfig(JSONObject data){
        List<MembershipConfig> configs = new ArrayList<>();
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
            MembershipConfig config = membershipConfigDao.findByName(key);
            if (config == null){
                config = new MembershipConfig(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }
}
