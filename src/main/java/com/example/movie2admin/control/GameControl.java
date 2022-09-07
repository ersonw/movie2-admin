package com.example.movie2admin.control;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.GameService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@Api(value = "api", tags = "短视频接口")
public class GameControl {
    @Autowired
    private GameService service;
    @GetMapping("/getGameList")
    public ResponseData getGameList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGame")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGame(@RequestBody pData data){
        return service.deleteGame(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateGame")
    @ApiGlobalModel(component = pData.class, value = "name,image,gameId,status,id")
    public ResponseData updateGame(@RequestBody pData data){
        return service.updateGame(data.getId(),data.getName(),data.getImage(),data.getGameId(),data.getStatus(),data.getUser(), data.getIp());
    }
    @PostMapping("/addGame")
    @ApiGlobalModel(component = pData.class, value = "name,image,gameId,status")
    public ResponseData addGame(@RequestBody pData data){
        return service.addGame(data.getName(),data.getImage(),data.getGameId(),data.getStatus(),data.getUser(), data.getIp());
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
    @GetMapping("/getButtonConfigList")
    public ResponseData getButtonConfigList(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getButtonConfigList(SysUser.getUser(user), ip);
    }
    @GetMapping("/getButtonConfig/{id}")
    public ResponseData getButtonConfig(
            @PathVariable long id,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getButtonConfig(id, SysUser.getUser(user), ip);
    }
    @GetMapping("/getGameConfig")
    public ResponseData getGameConfig(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameConfig(SysUser.getUser(user), ip);
    }
    @PostMapping("/updateGameConfig")
    public ResponseData updateGameConfig(@RequestBody JSONObject data){
        return service.updateGameConfig(data);
    }
}
