package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.ShortVideoService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortVideo")
@Api(value = "api", tags = "短视频接口")
public class ShortVideoControl {
    @Autowired
    private ShortVideoService service;
    @GetMapping("/getVideoList")
    public ResponseData getVideoList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getVideoList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteVideo")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteVideo(@RequestBody pData data){
        return service.deleteVideo(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateVideo")
    @ApiGlobalModel(component = pData.class, value = "title,pin,forward,status,id")
    public ResponseData updateVideo(@RequestBody pData data){
        return service.updateVideo(data.getId(),data.getTitle(),data.getPin(),data.getForward(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getShortVideoUser/{id}")
    public ResponseData getShortVideoUser(
            @PathVariable long id,
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getShortVideoUser(id,title,page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/getUser/{id}")
    public ResponseData getUser(
            @PathVariable long id,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getUser(id, SysUser.getUser(user), ip);
    }
    @GetMapping("/getAuditVideoList")
    public ResponseData getAuditVideoList(
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAuditVideoList(page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/getAuditVideo")
    public ResponseData getAuditVideo(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAuditVideo(SysUser.getUser(user), ip);
    }
    @PostMapping("/passAuditVideo")
    @ApiGlobalModel(component = pData.class, value = "id")
    public ResponseData passAuditVideo(@RequestBody pData data){
        return service.passAuditVideo(data.getId(),data.getUser(), data.getIp());
    }
    @PostMapping("/denyAuditVideo")
    @ApiGlobalModel(component = pData.class, value = "id")
    public ResponseData denyAuditVideo(@RequestBody pData data){
        return service.denyAuditVideo(data.getId(),data.getUser(), data.getIp());
    }
}
