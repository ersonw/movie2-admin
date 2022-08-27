package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.AVService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/av")
@Api(value = "api", tags = "看片接口")
public class AVControl {
    @Autowired
    private AVService service;
    @GetMapping("/getAVList")
    public ResponseData getAVList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAVList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/delete")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData delete(@RequestBody pData data){
        return service.delete(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/update")
    @ApiGlobalModel(component = pData.class, value = "plays,likes,title,id")
    public ResponseData update(@RequestBody pData data){
        return service.update(data.getId(),data.getPlays(),data.getLikes(),data.getTitle(),data.getUser(), data.getIp());
    }
    @PostMapping("/addPrice")
    @ApiGlobalModel(component = pData.class, value = "amount,ids")
    public ResponseData addPrice(@RequestBody pData data){
        return service.addPrice(data.getIds(),data.getAmount(),data.getUser(), data.getIp());
    }

    @GetMapping("/getTrashList")
    public ResponseData getTrashList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getTrashList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteTrash")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteTrash(@RequestBody pData data){
        return service.deleteTrash(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateTrash")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData updateTrash(@RequestBody pData data){
        return service.updateTrash(data.getIds(),data.getUser(), data.getIp());
    }

    @GetMapping("/getPriceList")
    public ResponseData getPriceList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getPriceList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deletePrice")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deletePrice(@RequestBody pData data){
        return service.deletePrice(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updatePrice")
    @ApiGlobalModel(component = pData.class, value = "amount,ids")
    public ResponseData updatePrice(@RequestBody pData data){
        return service.updatePrice(data.getIds(),data.getAmount(),data.getUser(), data.getIp());
    }
    @GetMapping("/getOrderList")
    public ResponseData getOrderList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getOrderList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteOrder(@RequestBody pData data){
        return service.deleteOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getCommentList")
    public ResponseData getCommentList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getCommentList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteComment")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteComment(@RequestBody pData data){
        return service.deleteComment(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/passComment")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData passComment(@RequestBody pData data){
        return service.passComment(data.getIds(),data.getUser(), data.getIp());
    }
}
