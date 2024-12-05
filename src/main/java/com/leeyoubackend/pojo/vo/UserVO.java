package com.leeyoubackend.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户包装类(脱敏)
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */

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
