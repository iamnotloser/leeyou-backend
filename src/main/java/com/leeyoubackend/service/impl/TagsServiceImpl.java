package com.leeyoubackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leeyoubackend.mapper.TagsMapper;
import com.leeyoubackend.pojo.Tags;
import com.leeyoubackend.service.TagsService;
import org.springframework.stereotype.Service;

/**
 * @author leeyou
 * @description 针对表【tags(标签表)】的数据库操作Service实现
 * @createDate 2024-11-29 16:49:22
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
        implements TagsService {

}
