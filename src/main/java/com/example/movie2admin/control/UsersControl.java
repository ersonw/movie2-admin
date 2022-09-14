package com.example.movie2admin.control;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.UsersService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Api(value = "api", tags = "用户管理接口")
public class UsersControl {
    @Autowired
    private UsersService service;
    @GetMapping("/getUserList")
    public ResponseData getUserList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUserList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteUser")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteUser(@RequestBody pData data){
        return service.deleteUser(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/unfreezeUser")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData unfreezeUser(@RequestBody pData data){
        return service.unfreezeUser(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/freezeUser")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData freezeUser(@RequestBody pData data){
        return service.freezeUser(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getConfig")
    public ResponseData getConfig(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getConfig(SysUser.getUser(user), ip);
    }
    @PostMapping("/updateConfig")
    public ResponseData updateConfig(@RequestBody JSONObject data){
        return service.updateConfig(data);
    }
    @GetMapping("/getUsersConsumeList")
    public ResponseData getUsersConsumeList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "type", required = false,defaultValue = "0") int type,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUsersConsumeList(title,type,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/getUsersConsumeListUser")
    public ResponseData getUsersConsumeListUser(
            @RequestParam(value = "id", required = false,defaultValue = "0") long userId,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUsersConsumeListUser(userId,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/makeupConsumeListUser")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeupConsumeListUser(@RequestBody pData data){
        return service.makeupConsumeListUser(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/makeDownConsumeListUser")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeDownConsumeListUser(@RequestBody pData data){
        return service.makeDownConsumeListUser(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getUsersSpreadRecordList")
    public ResponseData getUsersSpreadRecordList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUsersSpreadRecordList(title,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/getUsersSpreadRecordUserList")
    public ResponseData getUsersSpreadRecordUserList(
            @RequestParam(value = "id", required = false,defaultValue = "0") long userId,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUsersSpreadRecordUserList(userId,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteUsersSpreadRecordUserList")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteUsersSpreadRecordUserList(@RequestBody pData data){
        return service.deleteUsersSpreadRecordUserList(data.getIds(),data.getUser(), data.getIp());
    }
}
