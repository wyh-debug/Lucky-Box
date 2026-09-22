package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luckybox.pojo.dto.LuckyBoxCategorySelectDTO;
import com.luckybox.pojo.entity.LuckyBoxCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LuckyBoxCategoryMapper extends BaseMapper<LuckyBoxCategory> {
    Page<LuckyBoxCategory> selectCategoryPage(Page<LuckyBoxCategory> page, @Param("dto") LuckyBoxCategorySelectDTO luckyBoxCategorySelectDTO);
}
