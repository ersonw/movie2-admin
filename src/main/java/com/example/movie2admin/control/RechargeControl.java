package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.RechargeService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recharge")
@Api(value = "api", tags = "充值配置接口")
public class RechargeControl {
    @Autowired
    private RechargeService service;
    @GetMapping("/getChannelList")
    public ResponseData getChannelList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getChannelList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteChannel")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteChannel(@RequestBody pData data){
        return service.deleteChannel(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateChannel")
    @ApiGlobalModel(component = pData.class, value = "title,domain,mchId,secretKey,callbackUrl,notifyUrl,errorUrl,options,id")
    public ResponseData updateChannel(@RequestBody pData data){
        return service.updateChannel(data.getId(),data.getTitle(),data.getDomain(),data.getMchId(),data.getSecretKey(),data.getCallbackUrl(),data.getNotifyUrl(),data.getErrorUrl(),data.getOptions(),data.getUser(), data.getIp());
    }
    @PostMapping("/addChannel")
    @ApiGlobalModel(component = pData.class, value = "title,domain,mchId,secretKey,callbackUrl,notifyUrl,errorUrl,options")
    public ResponseData addChannel(@RequestBody pData data){
        return service.addChannel(data.getTitle(),data.getDomain(),data.getMchId(),data.getSecretKey(),data.getCallbackUrl(),data.getNotifyUrl(),data.getErrorUrl(),data.getOptions(),data.getUser(), data.getIp());
    }
    @GetMapping("/getOptionList")
    public ResponseData getOptionList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getOptionList(title,page,limit, SysUser.getUser(user), ip);
    }
}
