package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuckyBoxOrderMapper extends BaseMapper<LuckyBoxOrder> {
    @Select("select exists(select 1 from lucky_box_order where lucky_box_id = #{id})")
    boolean existsBox(@Param("id") Long id);
}
