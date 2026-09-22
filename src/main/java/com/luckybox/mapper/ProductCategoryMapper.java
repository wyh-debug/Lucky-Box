package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luckybox.pojo.dto.ProductCategorySelectDTO;
import com.luckybox.pojo.entity.ProductCategory;
import com.luckybox.pojo.vo.ProductCategoryPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    Page<ProductCategoryPageVO> selectPageProductCategory(Page<ProductCategoryPageVO> page, @Param("dto") ProductCategorySelectDTO productCategorySelectDTO);
    List<ProductCategoryPageVO> selectList();
}
