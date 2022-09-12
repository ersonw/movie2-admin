package com.example.movie2admin.data;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public  class pData  {
    public pData(){}

    @ApiModelProperty(name = "username", value= "用户账号或者手机号",required = false)
    private String username;
    @ApiModelProperty(name = "password", value= "用户密码",required = false)
    private String password;
    @ApiModelProperty(name = "deviceId", value= "设备ID",required = false)
    private String deviceId;
    @ApiModelProperty(name = "platform", value= "设备名称",required = false)
    private String platform;

    @ApiModelProperty(name = "phone", value= "手机号",required = false)
    private String phone;
    @ApiModelProperty(name = "codeId", value= "验证码ID",required = false)
    private String codeId;
    @ApiModelProperty(name = "code", value= "验证码",required = false)
    private String code;

    @ApiModelProperty(name = "seek", value= "视频刻度",required = false)
    private long seek;

    @ApiModelProperty(name = "id", value= "唯一ID",required = false)
    private long id;
    @ApiModelProperty(name = "toId", value= "目标ID",required = false)
    private long toId;
    @ApiModelProperty(name = "cashInId", value= "充值通道ID",required = false)
    private long cashInId;
    @ApiModelProperty(name = "gameId", value= "游戏ID",required = false)
    private int gameId;
    @ApiModelProperty(name = "game", value= "游戏",required = false)
    private String game;
    @ApiModelProperty(name = "less", value= "优惠",required = false)
    private int less;

    @ApiModelProperty(name = "duration", value= "目标ID",required = false)
    private long duration;

    @ApiModelProperty(name = "type", value= "类型",required = false)
    private int type;
    @ApiModelProperty(name = "forward", value= "转发",required = false)
    private int forward;
    @ApiModelProperty(name = "pin", value= "置顶",required = false)
    private int pin;
    @ApiModelProperty(name = "page", value= "页面",required = false)
    private int page;
    @ApiModelProperty(name = "status", value= "状态",required = false)
    private int status;
    @ApiModelProperty(name = "trial", value= "试用",required = false)
    private long trial;
    @ApiModelProperty(name = "text", value= "字符串",required = false)
    private String text;
    @ApiModelProperty(name = "pic", value= "图片",required = false)
    private String pic;
    @ApiModelProperty(name = "icon", value= "图标",required = false)
    private String icon;
    @ApiModelProperty(name = "image", value= "图片",required = false)
    private String image;
    @ApiModelProperty(name = "url1", value= "网址链接",required = false)
    private String url1;
    @ApiModelProperty(name = "name", value= "名称",required = false)
    private String name;
    @ApiModelProperty(name = "value", value= "值",required = false)
    private String value;
    @ApiModelProperty(name = "bank", value= "银行",required = false)
    private String bank;
    @ApiModelProperty(name = "card", value= "卡号",required = false)
    private String card;
    @ApiModelProperty(name = "address", value= "地址",required = false)
    private String address;
    @ApiModelProperty(name = "amount", value= "数量",required = false)
    private long amount;
    @ApiModelProperty(name = "plays", value= "播放基数",required = false)
    private long plays;
    @ApiModelProperty(name = "likes", value= "点赞基数",required = false)
    private long likes;
    @ApiModelProperty(name = "title", value= "标题",required = false)
    private String title;
    @ApiModelProperty(name = "price", value= "金额",required = false)
    private double price;

    @ApiModelProperty(name = "oss", value= "oss配置",required = false)
    private String oss;
    @ApiModelProperty(name = "ids", value= "ID列表",required = false)
    private List<Long> ids;
    public OssConfig getOss() {
        if (oss != null) return JSONObject.toJavaObject(JSONObject.parseObject(oss),OssConfig.class);
        return null;
    }

    @ApiModelProperty(name = "files", value= "文件列表",required = false)
    private String files;
    @ApiModelProperty(name = "filePath", value= "文件路径",required = false)
    private String filePath;
    @ApiModelProperty(name = "imagePath", value= "图片路径",required = false)
    private String imagePath;

    @ApiModelProperty(hidden = true)
    private String ip;
    @ApiModelProperty(hidden = true)
    private String serverName;
    @ApiModelProperty(hidden = true)
    private int serverPort;
    @ApiModelProperty(hidden = true)
    private String uri;
    @ApiModelProperty(hidden = true)
    private String url;
    @ApiModelProperty(hidden = true)
    private String schema;
    @ApiModelProperty(hidden = true)
    private boolean isWeb;
    @ApiModelProperty(hidden = true)
    private String user;

    public SysUser getUser() {
        JSONObject jsonObject = JSONObject.parseObject(user);
        return JSONObject.toJavaObject(jsonObject, SysUser.class);
    }
}
