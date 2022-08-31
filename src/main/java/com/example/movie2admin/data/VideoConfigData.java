package com.example.movie2admin.data;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class VideoConfigData {
    @ApiModelProperty(name = "files", value= "文件列表",required = false)
    private String files;
    @ApiModelProperty(hidden = true)
    private String ip;
    @ApiModelProperty(hidden = true)
    private String user;
    public SysUser getUser() {
        JSONObject jsonObject = JSONObject.parseObject(user);
        return JSONObject.toJavaObject(jsonObject, SysUser.class);
    }
}
