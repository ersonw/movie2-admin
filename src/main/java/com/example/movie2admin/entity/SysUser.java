package com.example.movie2admin.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "sys_user")
@Cacheable
@ToString(includeFieldNames = true)
public class SysUser {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String salt;
    private String password;
    private int status;
    private long addTime;
    private long updateTime;
    @Transient
    private String token;

    public static SysUser getUser(String user) {
        if(StringUtils.isEmpty(user)) return null;
        return JSONObject.toJavaObject(JSONObject.parseObject(user),SysUser.class);
    }
}
