package com.wyh.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.luckybox.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper extends BaseMapper<User> {
}
