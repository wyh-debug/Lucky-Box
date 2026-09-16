package com.wyh.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyh.luckybox.pojo.dto.ProductSelectDTO;
import com.wyh.luckybox.pojo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    Page<Product> getProductList(Page page, @Param("dto") ProductSelectDTO productSelectDTO);

}
