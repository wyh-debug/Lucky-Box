package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuckyBoxCardMapper extends BaseMapper<LuckyBoxCard> {
    @Select("select exists(select 1 from lucky_box_card where lucky_box_id = #{id})")
    boolean existBox(@Param("id") Long id);
}
