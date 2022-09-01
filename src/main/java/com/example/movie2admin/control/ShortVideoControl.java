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
}
