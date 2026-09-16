package com.wyh.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.luckybox.pojo.entity.SynthesisRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SynthesisRuleMapper extends BaseMapper<SynthesisRule> {

    @Select("select exists(select 1 from synthesis_rule where result_product_sku_id = #{id})")
    boolean existSynthesisRule(@Param("id") Long productSkuId);
}
