package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.SynthesisRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SynthesisRecordMapper extends BaseMapper<SynthesisRecord> {
    /**
     * 根据商品skuId查询是否有记录
     * @param productSkuId
     * @return
     */
    @Select("select exists(select 1 from synthesis_record where result_product_sku_id = #{id})")
    boolean existSynthesisRecord(@Param("id") Long productSkuId);
}
