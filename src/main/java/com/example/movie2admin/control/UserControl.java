package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.UserService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Api(value = "api", tags = "用户接口")
public class UserControl {
    @Autowired
    private UserService service;
    @PostMapping("/login")
    @ApiGlobalModel(component = pData.class, value = "username,password")
    public ResponseData login(@RequestBody pData data){
        if (StringUtils.isEmpty(data.getUsername()) || StringUtils.isEmpty(data.getPassword())){
            return ResponseData.error("用户名或密码必填!");
        }
        return service.login(data.getUsername(),data.getPassword(), data.getIp());
    }
    @GetMapping("/info")
    public ResponseData info(@RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
                                @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.info(SysUser.getUser(user), ip);
    }
    @GetMapping("/logout")
    public ResponseData logout(@RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
                                @RequestParam(value = "user") @ApiParam(hidden = true) String user){
        return service.logout(SysUser.getUser(user), ip);
    }
    @GetMapping("/test")
    public ResponseData test(){
        return service.test();
    }
}
