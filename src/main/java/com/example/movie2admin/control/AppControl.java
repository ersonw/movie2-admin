package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.AppService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app")
@Api(value = "api", tags = "app接口")
public class AppControl {
    @Autowired
    private AppService service;
    @GetMapping("/getAppConfig")
    public ResponseData getAppConfig(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAppConfig(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteAppConfig")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteAppConfig(@RequestBody pData data){
        return service.deleteAppConfig(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateAppConfig")
    @ApiGlobalModel(component = pData.class, value = "trial,plays,likes,title,id")
    public ResponseData updateAppConfig(@RequestBody pData data){
        return service.updateAppConfig(data.getTrial(),data.getId(),data.getPlays(),data.getLikes(),data.getTitle(),data.getUser(), data.getIp());
    }
    @PostMapping("/addAppConfig")
    @ApiGlobalModel(component = pData.class, value = "amount,ids")
    public ResponseData addAppConfig(@RequestBody pData data){
        return service.addAppConfig(data.getIds(),data.getAmount(),data.getUser(), data.getIp());
    }
}
