package com.example.movie2admin.control;

import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.pData;
import com.example.movie2admin.entity.SysUser;
import com.example.movie2admin.service.MembershipService;
import com.example.movie2admin.util.ApiGlobalModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
