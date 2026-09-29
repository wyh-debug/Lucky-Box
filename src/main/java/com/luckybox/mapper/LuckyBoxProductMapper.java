package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.pojo.vo.LuckyBoxProductItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface LuckyBoxProductMapper extends BaseMapper<LuckyBoxProduct> {
    @Select("select exists(select 1 from lucky_box_product where product_sku_id = #{id})")
    boolean existLuckyBoxProduct(@Param("id") Long productSkuId);

    @Select("select exists(select 1 from lucky_box_product where lucky_box_id = #{id})")
    boolean existsBox(@Param("id") Long id);

    boolean insertBatch(@Param("ls") List<LuckyBoxProduct> luckyBoxProducts);

    List<LuckyBoxProduct> getBatchByBoxIds(@Param("boxIds") Set<Long> boxIds);

    List<LuckyBoxProductItemVO> getBoxProductList(@Param("ids") Set<Long> boxIds);

    LuckyBoxProductItemVO getBoxProduct(Long id);
}
