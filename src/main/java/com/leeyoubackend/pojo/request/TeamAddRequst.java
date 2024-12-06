package com.leeyoubackend.pojo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
public class TeamAddRequst implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * 队伍密码
     */
    private String password;

    /**
     * 过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd ")
    private Date expireTime;

    /**
     * 是否有效 0公开 1 私有 2 加密
     */
    private Integer status;



}
