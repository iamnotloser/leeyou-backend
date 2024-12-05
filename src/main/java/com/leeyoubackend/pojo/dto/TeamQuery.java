package com.leeyoubackend.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.leeyoubackend.pojo.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 队伍查询包装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * id
     */

    private Long id;
    /**
     * 搜索关键词(同时对name和description进行搜索)
     */
    private String searchText;
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
     * 状态
     */
    private Integer status;

}
