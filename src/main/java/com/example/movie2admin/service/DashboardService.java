package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.Dashboard;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DashboardService {
    @Autowired
    private CashInOrderDao cashInOrderDao;
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

    public ResponseData getAll(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        Dashboard dashboard = new Dashboard();
        dashboard.setCashInAll(cashInOrderDao.getAll());
        dashboard.setCashInToday(cashInOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashInYesterday(cashInOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setCashOutAll(gameOutOrderDao.getAll());
        dashboard.setCashOutToday(gameOutOrderDao.getAll(TimeUtil.getTodayZero()));
        dashboard.setCashOutYesterday(gameOutOrderDao.getAll(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setCashOutOrders(gameOutOrderDao.countAllByStatus(0));

        dashboard.setUsersAll(userDao.count());
        dashboard.setUsersToday(userDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setUsersYesterday(userDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setMembershipAll(membershipExpiredDao.count());
        dashboard.setMembershipToday(membershipExpiredDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setMembershipYesterday(membershipExpiredDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setVideosAll(videoDao.count());
        dashboard.setVideosToday(videoDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setVideosYesterday(videoDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));

        dashboard.setShortAll(shortVideoDao.count());
        dashboard.setShortToday(shortVideoDao.countAllByAddTimeGreaterThanEqual(TimeUtil.getTodayZero()));
        dashboard.setShortYesterday(shortVideoDao.countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero()));
        dashboard.setShortAudit(shortVideoDao.countAllByStatus(0));

        return ResponseData.success(JSONObject.parseObject(dashboard.toString()));
    }
}
