package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.AuthDao;
import com.example.movie2admin.dao.SysUserDao;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.util.MD5Util;
import com.example.movie2admin.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private AuthDao authDao;
    @Autowired
    private SysUserDao sysUserDao;
    public ResponseData login(String username, String password, String ip) {
        log.info("Login[username:{}, password:{}, ip:{}]", username, password, ip);
        SysUser user = sysUserDao.findAllByUsername(username.trim());
        if (user == null) return ResponseData.error("用户不存在！");
        MD5Util md5Util = new MD5Util(user.getSalt());
        String pass = md5Util.getPassWord(password);
//        System.out.println(pass);
        if (pass.equals(user.getPassword())){
            user.setToken(ToolsUtil.getToken());
            authDao.pushUser(user);
            return ResponseData.success(ResponseData.object("token",user.getToken()));
        }
        return ResponseData.error("登录失败！密码错误");
    }

    public ResponseData info(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201,"登录已过期！");
        SysUser profile = sysUserDao.findAllById(user.getId());
        if (profile == null || profile.getStatus() != 1) return ResponseData.error(201,"用户不存在！");
        profile.setToken(user.getToken());
        authDao.pushUser(profile);
        JSONObject json = new JSONObject();
        json.put("name", profile.getUsername());
        JSONArray roles = new JSONArray();
        roles.add("admin");
        json.put("roles", roles);
        json.put("introduction","我是管理员");
        json.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ResponseData.success(json);
    }

    public ResponseData logout(SysUser user, String ip) {
        if(user == null) return ResponseData.success();
        authDao.popUser(user);
        return ResponseData.success();
    }

    public ResponseData test() {
        SysUser profile = new SysUser();
        profile.setUsername("admin");
        profile.setPassword("Admin@123!");

        profile.setAddTime(System.currentTimeMillis());
        profile.setStatus(1);
        profile.setSalt(ToolsUtil.getSalt());
        MD5Util md5Util = new MD5Util(profile.getSalt());
        profile.setPassword(md5Util.getPassWord(md5Util.getMD5(profile.getPassword())));
        profile.setUpdateTime(System.currentTimeMillis());
//        sysUserDao.saveAndFlush(profile);
        return ResponseData.success();
    }
}
