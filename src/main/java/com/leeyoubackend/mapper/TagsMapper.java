package com.leeyoubackend.mapper;

import com.leeyoubackend.pojo.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author leeyou
* @description 针对表【tags(标签表)】的数据库操作Mapper
* @createDate 2024-11-29 16:49:22
* @Entity com.usercenter.pojo.Tags
*/
@Mapper
public interface TagsMapper extends BaseMapper<Tags> {

}




