package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.pojo.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    //查询是否存在商品sku
    @Select("select exists(select 1 from product_sku where product_id = #{id})")
    boolean existsProductSku(@Param("id") Long productId);

    //根据id批量减库存
    boolean updateBatch(@Param("dto") List<LuckyBoxProduct> boxProducts);
}
