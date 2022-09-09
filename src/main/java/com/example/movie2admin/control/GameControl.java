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
@Api(value = "api", tags = "游戏接口")
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
    @GetMapping("/getGameOrderList")
    public ResponseData getGameOrderList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameOrderList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGameOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGameOrder(@RequestBody pData data){
        return service.deleteGameOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/makeupGameOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeupGameOrder(@RequestBody pData data){
        return service.makeupGameOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getGameWithdrawConfig")
    public ResponseData getGameWithdrawConfig(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameWithdrawConfig(SysUser.getUser(user), ip);
    }
    @PostMapping("/updateGameWithdrawConfig")
    public ResponseData updateGameWithdrawConfig(@RequestBody JSONObject data){
        return service.updateGameWithdrawConfig(data);
    }
    @GetMapping("/getGameWithdrawOrder")
    public ResponseData getGameWithdrawOrder(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "enabled", required = false,defaultValue = "false") boolean enabled,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameWithdrawOrder(title,enabled,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/makeDownGameWithdrawOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeDownGameWithdrawOrder(@RequestBody pData data){
        return service.makeDownGameWithdrawOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/makeupGameWithdrawOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeupGameWithdrawOrder(@RequestBody pData data){
        return service.makeupGameWithdrawOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getGameWithdrawCard")
    public ResponseData getGameWithdrawCard(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "enabled", required = false,defaultValue = "false") boolean enabled,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameWithdrawCard(title,enabled,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGameWithdrawCard")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGameWithdrawCard(@RequestBody pData data){
        return service.deleteGameWithdrawCard(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateGameWithdrawCard")
    @ApiGlobalModel(component = pData.class, value = "name,bank,card,address,id")
    public ResponseData updateGameWithdrawCard(@RequestBody pData data){
        return service.updateGameWithdrawCard(data.getId(),data.getName(),data.getBank(),data.getCard(),data.getAddress(),data.getUser(), data.getIp());
    }
    @GetMapping("/getGameWaterList")
    public ResponseData getGameWaterList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "type", required = false,defaultValue = "0") int type,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameWaterList(title,type,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/getGameWater")
    public ResponseData getGameWater(
            @RequestParam(value = "id", required = false,defaultValue = "0") long userId,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameWater(userId,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @GetMapping("/updateGameWater")
    public ResponseData updateGameWater(
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.updateGameWater(SysUser.getUser(user), ip);
    }
    @GetMapping("/getGamePublicity")
    public ResponseData getGamePublicity(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGamePublicity(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGamePublicity")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGamePublicity(@RequestBody pData data){
        return service.deleteGamePublicity(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateGamePublicity")
    @ApiGlobalModel(component = pData.class, value = "name,pic,url1,status,type,id")
    public ResponseData updateGamePublicity(@RequestBody pData data){
        return service.updateGamePublicity(data.getId(),data.getName(),data.getPic(),data.getUrl1(),data.getStatus(),data.getType(),data.getUser(), data.getIp());
    }
    @PostMapping("/addGamePublicity")
    @ApiGlobalModel(component = pData.class, value = "name,pic,url1,status,type")
    public ResponseData addGamePublicity(@RequestBody pData data){
        return service.addGamePublicity(data.getName(),data.getPic(),data.getUrl1(),data.getStatus(),data.getType(),data.getUser(), data.getIp());
    }
    @GetMapping("/getGameScroll")
    public ResponseData getGameScroll(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGameScroll(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGameScroll")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGameScroll(@RequestBody pData data){
        return service.deleteGameScroll(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateGameScroll")
    @ApiGlobalModel(component = pData.class, value = "name,amount,game,id")
    public ResponseData updateGameScroll(@RequestBody pData data){
        return service.updateGameScroll(data.getId(),data.getName(),data.getAmount(),data.getGame(),data.getUser(), data.getIp());
    }
    @PostMapping("/addGameScroll")
    @ApiGlobalModel(component = pData.class, value =  "name,amount,game,id")
    public ResponseData addGameScroll(@RequestBody pData data){
        return service.addGameScroll(data.getName(),data.getAmount(),data.getGame(),data.getUser(), data.getIp());
    }
    @PostMapping("/automaticGameScroll")
    @ApiGlobalModel(component = pData.class, value = "amount")
    public ResponseData automaticGameScroll(@RequestBody pData data){
        return service.automaticGameScroll(data.getAmount(),data.getUser(), data.getIp());
    }
}
