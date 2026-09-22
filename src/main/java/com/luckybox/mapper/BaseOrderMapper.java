package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.BaseOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseOrderMapper extends BaseMapper<BaseOrder> {
}
