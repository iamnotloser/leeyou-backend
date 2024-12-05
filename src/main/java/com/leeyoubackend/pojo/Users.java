package com.leeyoubackend.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户表
 * @TableName users
 */

@TableName(value ="user_leeyou")
@Data
public class Users implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账户名称
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别 1 男 0 女
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否有效 0有效 
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 1 已删除 0 未删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 权限 0 默认角色 1 管理员 
     */
    private Integer role;

    /**
     * 星球编号
     */
    private String planetCode;
    private String tags;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}