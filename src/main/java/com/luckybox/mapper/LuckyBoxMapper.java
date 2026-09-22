package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuckyBoxMapper extends BaseMapper<LuckyBox> {
    @Select("SELECT EXISTS(SELECT 1 FROM lucky_box WHERE category_id = #{categoryId})")
    boolean existsByCategoryId(@Param("categoryId") Long categoryId);
}
