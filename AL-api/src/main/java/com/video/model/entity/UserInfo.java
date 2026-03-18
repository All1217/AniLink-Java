package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "用户信息表")
@TableName(value = "user")
@Data
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户唯一标识")
    @TableId(type = IdType.INPUT)
    private Long uid;

    @Schema(description = "账号，暂时设置为与uid等同")
    @TableField(value = "username")
    private String username;

    @Schema(description = "密码")
    @TableField(value = "password", select = false)
    private String password;

    @Schema(description = "用户名")
    @TableField(value = "nickname")
    private String nickname;

    @Schema(description = "头像url")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "主页背景图")
    @TableField(value = "background")
    private String background;

    @Schema(description = "性别")
    @TableField(value = "gender")
    private Integer gender;

    @Schema(description = "个性签名")
    @TableField(value = "description")
    private String description;

    @Schema(description = "经验值")
    @TableField(value = "exp")
    private Integer exp;

    @Schema(description = "硬币数")
    @TableField(value = "coin")
    private Double coin;

    @Schema(description = "会员类型")
    @TableField(value = "vip")
    private Integer vip;

    @Schema(description = "账号状态")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "账号角色")
    @TableField(value = "role")
    private Integer role;

    @Schema(description = "认证类型")
    @TableField(value = "auth")
    private Short auth;

    @Schema(description = "认证说明")
    @TableField(value = "auth_msg")
    private String authMsg;

    @Schema(description = "创建时间")
    @TableField(value = "create_date")
    @JsonIgnore
    private Date createDate;

    @Schema(description = "注销时间")
    @TableField(value = "delete_date")
    @JsonIgnore
    private Date deleteDate;
}
