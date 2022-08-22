package com.example.movie2admin.util;

import com.example.movie2admin.dao.AuthDao;
import com.example.movie2admin.dao.SysUserDao;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class Utils {
    private static Utils self;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private SysUserDao sysUserDao;

    @PostConstruct
    public void initPost(){
        self = this;
        System.out.printf("加载器成功！\n");
    }
    public static AuthDao getAuthDao(){
        return self.authDao;
    }
    public static SysUserDao getUserRepository(){
        return self.sysUserDao;
    }
}
