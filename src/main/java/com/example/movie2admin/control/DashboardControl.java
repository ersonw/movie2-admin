package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Api(value = "api", tags = "仪表盘接口")
public class DashboardControl {
    @Autowired
    private DashboardService service;
    @GetMapping("/info")
    public ResponseData info(){
        return service.info();
    }
    @GetMapping("/getAll")
    public ResponseData getAll(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAll( SysUser.getUser(user), ip);
    }
}
