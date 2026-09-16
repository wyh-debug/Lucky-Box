package com.wyh.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.luckybox.pojo.entity.ProductOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    @Select("select exists(select 1 from product_order where product_sku_id = #{id})")
    boolean existsProductOrder(@Param("id") Long productSkuId);
}
