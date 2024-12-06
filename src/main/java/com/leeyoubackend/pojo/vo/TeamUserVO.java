package com.leeyoubackend.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍和用户信息封装类
 */
@Data
public class TeamUserVO implements Serializable {

    /**
     * id
     */

    private Long id;

    /**
     * 队伍名
     */
    private String name;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 队伍描述
     */
    private String description;


    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 是否有效 0公开 1 私有 2 加密
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


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 入队用户列表
     */
    UserVO createUser;
}
