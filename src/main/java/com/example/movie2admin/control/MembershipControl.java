package com.example.movie2admin.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.service.MembershipService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/membership")
@Api(value = "api", tags = "会员接口")
public class MembershipControl {
    @Autowired
    private MembershipService service;
    @GetMapping("/getMembershipList")
    public ResponseData getMembershipList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getMembershipList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteMembership")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteMembership(@RequestBody pData data){
        return service.deleteMembership(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/expiredMembership")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData expiredMembership(@RequestBody pData data){
        return service.expiredMembership(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getMembershipListOrder")
    public ResponseData getMembershipListOrder(
            @RequestParam(value = "id", required = false,defaultValue = "0") long userId,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getMembershipListOrder(userId,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteMembershipListOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteMembershipListOrder(@RequestBody pData data){
        return service.deleteMembershipListOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getMembershipListExperience")
    public ResponseData getMembershipListExperience(
            @RequestParam(value = "id", required = false,defaultValue = "0") long userId,
            @RequestParam(value = "start", required = false,defaultValue = "0") long start,
            @RequestParam(value = "end", required = false,defaultValue = "0") long end,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getMembershipListExperience(userId,start,end,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteMembershipListExperience")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteMembershipListExperience(@RequestBody pData data){
        return service.deleteMembershipListExperience(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getBenefitList")
    public ResponseData getBenefitList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getBenefitList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteBenefit")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteBenefit(@RequestBody pData data){
        return service.deleteBenefit(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateBenefit")
    @ApiGlobalModel(component = pData.class, value = "name,icon,id")
    public ResponseData updateBenefit(@RequestBody pData data){
        return service.updateBenefit(data.getId(),data.getName(),data.getIcon(),data.getUser(), data.getIp());
    }
    @PostMapping("/addBenefit")
    @ApiGlobalModel(component = pData.class, value = "name,icon")
    public ResponseData addBenefit(@RequestBody pData data){
        return service.addBenefit(data.getName(),data.getIcon(),data.getUser(), data.getIp());
    }
    @GetMapping("/getGradeList")
    public ResponseData getGradeList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getGradeList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteGrade")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteGrade(@RequestBody pData data){
        return service.deleteGrade(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateGrade")
    @ApiGlobalModel(component = pData.class, value = "name,icon,mini,max,options,status,id")
    public ResponseData updateGrade(@RequestBody pData data){
        return service.updateGrade(data.getId(),data.getName(),data.getIcon(),data.getMini(),data.getMax(),data.getOptions(),data.getStatus(), data.getUser(), data.getIp());
    }
    @PostMapping("/addGrade")
    @ApiGlobalModel(component = pData.class, value = "name,icon,mini,max,options,status")
    public ResponseData addGrade(@RequestBody pData data){
        return service.addGrade(data.getName(),data.getIcon(),data.getMini(),data.getMax(),data.getOptions(), data.getStatus(), data.getUser(), data.getIp());
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
    @ApiGlobalModel(component = pData.class, value = "amount,price,original,gameCoin,experience,cashInId,status,id")
    public ResponseData updateButton(@RequestBody pData data){
        return service.updateButton(data.getId(),data.getAmount(),data.getPrice(),data.getOriginal(),data.getGameCoin(),data.getExperience(),data.getCashInId(),data.getStatus(),data.getUser(), data.getIp());
    }
    @PostMapping("/addButton")
    @ApiGlobalModel(component = pData.class, value = "amount,price,original,gameCoin,experience,cashInId,status")
    public ResponseData addButton(@RequestBody pData data){
        return service.addButton(data.getAmount(),data.getPrice(),data.getOriginal(),data.getGameCoin(),data.getExperience(),data.getCashInId(),data.getStatus(),data.getUser(), data.getIp());
    }
    @GetMapping("/getOrderList")
    public ResponseData getOrderList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getOrderList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteOrder(@RequestBody pData data){
        return service.deleteOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/makeupOrder")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData makeupOrder(@RequestBody pData data){
        return service.makeupOrder(data.getIds(),data.getUser(), data.getIp());
    }
    @GetMapping("/getLevelList")
    public ResponseData getLevelList(
            @RequestParam(value = "title", required = false,defaultValue = "") String title,
            @RequestParam(value = "page", required = false,defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false,defaultValue = "20") int limit,
            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user){
        return service.getLevelList(title,page,limit, SysUser.getUser(user), ip);
    }
    @PostMapping("/deleteLevel")
    @ApiGlobalModel(component = pData.class, value = "ids")
    public ResponseData deleteLevel(@RequestBody pData data){
        return service.deleteLevel(data.getIds(),data.getUser(), data.getIp());
    }
    @PostMapping("/updateLevel")
    @ApiGlobalModel(component = pData.class, value = "level,experience,id")
    public ResponseData updateLevel(@RequestBody pData data){
        return service.updateLevel(data.getId(),data.getLevel(),data.getExperience(),data.getUser(), data.getIp());
    }
    @PostMapping("/addLevel")
    @ApiGlobalModel(component = pData.class, value = "level,experience")
    public ResponseData addLevel(@RequestBody pData data){
        return service.addLevel(data.getLevel(),data.getExperience(),data.getUser(), data.getIp());
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

}
