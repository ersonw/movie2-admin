package com.example.movie2admin.control;

import com.alibaba.fastjson.JSONObject;
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
    @GetMapping("/getClassListVideo")
    public ResponseData getClassListVideo(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getClassListVideo(SysUser.getUser(user), ip);
    }
    @GetMapping("/getAVList")
    public ResponseData getAVList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getAVList(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/updateClassVideo")
    @ApiGlobalModel(component = pData.class, value = "ids,id")
    public ResponseData updateClassVideo(@RequestBody pData data){
        return service.updateClassVideo(data.getIds(),data.getId(),data.getUser(), data.getIp());
    }
    @PostMapping("/delete")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData delete(@RequestBody pData data){
        return service.delete(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/update")
    @ApiGlobalModel(component = pData.class, value = "trial,plays,likes,title,id")
    public ResponseData update(@RequestBody pData data){
        return service.update(data.getTrial(),data.getId(),data.getPlays(),data.getLikes(),data.getTitle(),data.getUser(), data.getIp());
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
    @GetMapping("/getConcentration")
    public ResponseData getConcentration(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getConcentration(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteConcentration")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteConcentration(@RequestBody pData data){
        return service.deleteConcentration(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/addConcentration")
    @ApiGlobalModel(component = pData.class, value = "name,px")
    public ResponseData addConcentration(@RequestBody pData data){
        return service.addConcentration(data.getName(),data.getPx(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateConcentration")
    @ApiGlobalModel(component = pData.class, value = "px,name,id")
    public ResponseData updateConcentration(@RequestBody pData data){
        return service.updateConcentration(data.getId(),data.getName(),data.getPx(),data.getUser(), data.getIp());
    }
    @GetMapping("/getConcentrationList/{id}")
    public ResponseData getConcentrationList(
            @PathVariable long id,
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getConcentrationList(id,title,page,limit,SysUser.getUser(user), ip);
    }
    @GetMapping("/getActiveList/{id}")
    public ResponseData getActiveList(
            @PathVariable long id,
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getActiveList(id,title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteConcentrationList")
    @ApiGlobalModel(component = pData.class, value = "ids,id")
    public ResponseData deleteConcentrationList(@RequestBody pData data){
        return service.deleteConcentrationList(data.getIds(),data.getId(),data.getUser(), data.getIp());
    }
    @PostMapping("/addConcentrationList")
    @ApiGlobalModel(component = pData.class, value = "ids,id")
    public ResponseData addConcentrationList(@RequestBody pData data){
        return service.addConcentrationList(data.getIds(),data.getId(),data.getUser(), data.getIp());
    }
    @GetMapping("/getVideoSource")
    public ResponseData getVideoSource(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getVideoSource(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteVideoSource")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteVideoSource(@RequestBody pData data){
        return service.deleteVideoSource(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/addVideoSource")
    @ApiGlobalModel(component = pData.class, value = "name,status")
    public ResponseData addVideoSource(@RequestBody pData data){
        return service.addVideoSource(data.getName(), data.getStatus(), data.getUser(), data.getIp());
    }
    @PostMapping("/updateVideoSource")
    @ApiGlobalModel(component = pData.class, value = "name,status,id")
    public ResponseData updateVideoSource(@RequestBody pData data){
        return service.updateVideoSource(data.getId(),data.getName(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getVideoSourceList/{id}")
    public ResponseData getVideoSourceList(
            @PathVariable long id,
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getVideoSourceList(id,title,page,limit,SysUser.getUser(user), ip);
    }
    @GetMapping("/getVideoSourceActiveList/{id}")
    public ResponseData getVideoSourceActiveList(
            @PathVariable long id,
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getVideoSourceActiveList(id,title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteVideoSourceList")
    @ApiGlobalModel(component = pData.class, value = "ids,id")
    public ResponseData deleteVideoSourceList(@RequestBody pData data){
        return service.deleteVideoSourceList(data.getIds(),data.getId(),data.getUser(), data.getIp());
    }
    @PostMapping("/addVideoSourceList")
    @ApiGlobalModel(component = pData.class, value = "ids,id")
    public ResponseData addVideoSourceList(@RequestBody pData data){
        return service.addVideoSourceList(data.getIds(),data.getId(),data.getUser(), data.getIp());
    }
    @GetMapping("/getIndexPublicity")
    public ResponseData getIndexPublicity(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getIndexPublicity(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteIndexPublicity")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteIndexPublicity(@RequestBody pData data){
        return service.deleteIndexPublicity(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/addIndexPublicity")
    @ApiGlobalModel(component = pData.class, value = "name,pic,url1,type,status")
    public ResponseData addIndexPublicity(@RequestBody pData data){
        return service.addIndexPublicity(data.getName(),data.getPic(),data.getUrl1(),data.getType(), data.getStatus(), data.getUser(), data.getIp());
    }
    @PostMapping("/updateIndexPublicity")
    @ApiGlobalModel(component = pData.class, value = "name,pic,url1,type,status,id")
    public ResponseData updateIndexPublicity(@RequestBody pData data){
        return service.updateIndexPublicity(data.getId(),data.getName(),data.getPic(),data.getUrl1(),data.getType(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getPlayPublicity")
    public ResponseData getPlayPublicity(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getPlayPublicity(title,page,limit,SysUser.getUser(user), ip);
    }
    @PostMapping("/deletePlayPublicity")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deletePlayPublicity(@RequestBody pData data){
        return service.deletePlayPublicity(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/addPlayPublicity")
    @ApiGlobalModel(component = pData.class, value = "name,image,url1,type,page,status")
    public ResponseData addPlayPublicity(@RequestBody pData data){
        return service.addPlayPublicity(data.getName(),data.getImage(),data.getUrl1(),data.getType(),data.getPage(), data.getStatus(), data.getUser(), data.getIp());
    }
    @PostMapping("/updatePlayPublicity")
    @ApiGlobalModel(component = pData.class, value = "name,pic,url1,type,status,id")
    public ResponseData updatePlayPublicity(@RequestBody pData data){
        return service.updatePlayPublicity(data.getId(),data.getName(),data.getImage(),data.getUrl1(),data.getType(),data.getPage(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getVideoConfig")
    public ResponseData getVideoConfig(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getVideoConfig(SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteVideoConfig")
    @ApiGlobalModel(component = pData.class, value = "name")
    public ResponseData deleteVideoConfig(@RequestBody pData data){
        return service.deleteVideoConfig(data.getName(),data.getUser(), data.getIp());
    }
    @PostMapping("/addVideoConfig")
    @ApiGlobalModel(component = pData.class, value = "name,value")
    public ResponseData addVideoConfig(@RequestBody pData data){
        return service.addVideoConfig(data.getName(),data.getValue(), data.getUser(), data.getIp());
    }
    @PostMapping("/updateVideoConfig")
    public ResponseData updateVideoConfig(@RequestBody JSONObject data){
        return service.updateVideoConfig(data);
    }
    @GetMapping("/getDiamondConfig")
    public ResponseData getDiamondConfig(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getDiamondConfig(SysUser.getUser(user), ip);
    }
    @PostMapping("/updateDiamondConfig")
    public ResponseData updateDiamondConfig(@RequestBody JSONObject data){
        return service.updateDiamondConfig(data);
    }
    @GetMapping("/getButtonList")
    public ResponseData getButtonList(
            @RequestParam(value = "title", required = false,defaultValue = "0") long title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getButtonList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteButton")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteButton(@RequestBody pData data){
        return service.deleteButton(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateButton")
    @ApiGlobalModel(component = pData.class, value = "amount,price,less,cashInId,status,id")
    public ResponseData updateButton(@RequestBody pData data){
        return service.updateButton(data.getId(),data.getAmount(),data.getPrice(),data.getLess(),data.getCashInId(),data.getStatus(),data.getUser(), data.getIp());
    }
    @PostMapping("/addButton")
    @ApiGlobalModel(component = pData.class, value = "amount,price,less,cashInId,status")
    public ResponseData addButton(@RequestBody pData data){
        return service.addButton(data.getAmount(),data.getPrice(),data.getLess(),data.getCashInId(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getDiamondOrderList")
    public ResponseData getDiamondOrderList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getDiamondOrderList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteDiamondOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteDiamondOrder(@RequestBody pData data){
        return service.deleteDiamondOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/makeupDiamondOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeupDiamondOrder(@RequestBody pData data){
        return service.makeupDiamondOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getClassList")
    public ResponseData getClassList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getClassList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteClass")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteClass(@RequestBody pData data){
        return service.deleteClass(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateClass")
    @ApiGlobalModel(component = pData.class, value = "px,name,id")
    public ResponseData updateClass(@RequestBody pData data){
        return service.updateClass(data.getId(),data.getName(),data.getPx(),data.getUser(), data.getIp());
    }
    @PostMapping("/addClass")
    @ApiGlobalModel(component = pData.class, value = "name,px")
    public ResponseData addClass(@RequestBody pData data){
        return service.addClass(data.getName(),data.getPx(),data.getUser(), data.getIp());
    }
}
