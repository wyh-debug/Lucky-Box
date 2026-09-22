package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    //查询是否存在商品sku
    @Select("select exists(select 1 from product_sku where product_id = #{id})")
    boolean existsProductSku(@Param("id") Long productId);
}
