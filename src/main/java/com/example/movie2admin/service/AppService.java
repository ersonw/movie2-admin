package com.example.movie2admin.service;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppService {
    public ResponseData getAppConfig(String title, int page, int limit, SysUser user, String ip) {
        return ResponseData.success();
    }

    public ResponseData deleteAppConfig(List<Long> ids, SysUser user, String ip) {
        return ResponseData.success();
    }

    public ResponseData updateAppConfig(long trial, long id, long plays, long likes, String title, SysUser user, String ip) {
        return ResponseData.success();
    }

    public ResponseData addAppConfig(List<Long> ids, long amount, SysUser user, String ip) {
        return ResponseData.success();
    }
}
