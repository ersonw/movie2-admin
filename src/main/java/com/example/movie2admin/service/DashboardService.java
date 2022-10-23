package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.Dashboard;
import com.example.movie2admin.data.InfoData;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DashboardService {
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private GameOrderDao gameOrderDao;
    @Autowired
    private MembershipOrderDao membershipOrderDao;
    @Autowired
    private DiamondOrderDao diamondOrderDao;
    @Autowired
    private GameOutOrderDao gameOutOrderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MembershipExpiredDao membershipExpiredDao;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private ShortVideoDao shortVideoDao;
    @Autowired
    private AuthDao authDao;

    public ResponseData getAll(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        Dashboard dashboard = new Dashboard();
        dashboard.setCashInAll(cashInOrderDao.getAll());
        dashboard.setCashInToday(cashInOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashInYesterday(cashInOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setCashInGameAll(gameOrderDao.getAll());
        dashboard.setCashInGameToday(gameOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashInGameYesterday(gameOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setCashOutAll(gameOutOrderDao.getAll());
        dashboard.setCashOutToday(gameOutOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashOutYesterday(gameOutOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setCashOutOrders(gameOutOrderDao.countAllByStatus(0));

        dashboard.setUsersAll(userDao.count());
        dashboard.setUsersToday(userDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setUsersYesterday(userDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setCashInMembershipAll(membershipOrderDao.getAll());
        dashboard.setCashInMembershipToday(membershipOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashInMembershipYesterday(membershipOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setMembershipAll(membershipExpiredDao.count());
        dashboard.setMembershipToday(membershipExpiredDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setMembershipYesterday(membershipExpiredDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setCashInDiamondAll(diamondOrderDao.getAll());
        dashboard.setCashInDiamondToday(diamondOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashInDiamondYesterday(diamondOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setVideosAll(videoDao.count());
        dashboard.setVideosToday(videoDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setVideosYesterday(videoDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setShortAll(shortVideoDao.count());
        dashboard.setShortToday(shortVideoDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setShortYesterday(shortVideoDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setShortAudit(shortVideoDao.countAllByStatus(0));

        InfoData data = authDao.findInfoDataType(0);
        if (data != null) dashboard.setOnline(data.getCount());
        return ResponseData.success(JSONObject.parseObject(dashboard.toString()));
    }

    public ResponseData info() {
        return ResponseData.success(JSONArray.parseArray(JSONArray.toJSONString(authDao.getInfos())));
    }
}
