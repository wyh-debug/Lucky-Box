package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxProductRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MysteryBoxProductRelMapper extends BaseMapper<LuckyBoxProductRel> {
    @Select("select exists(select 1 from mystery_box_product_rel where product_sku_id = #{id})")
    boolean existMysteryBoxProductRel(@Param("id") Long productSkuId);
}
