package com.wyh.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.luckybox.pojo.dto.ProductCategorySelectDTO;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.ProductCategory;
import com.wyh.luckybox.mapper.ProductCategoryMapper;
import com.wyh.luckybox.pojo.vo.ProductCategoryPageVO;
import com.wyh.luckybox.service.IProductCategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements IProductCategoryService {

    @Resource
    private ProductCategoryMapper productCategoryMapper;
    @Override
    public Result pageProductCategory(ProductCategorySelectDTO productCategorySelectDTO) {
        Page<ProductCategoryPageVO> page = new Page<>(productCategorySelectDTO.getPage(), productCategorySelectDTO.getSize());
        Page<ProductCategoryPageVO> productCategoryPageVOPage = productCategoryMapper.selectPageProductCategory(page, productCategorySelectDTO);
        //List<ProductCategoryPageVO> list = productCategoryPageVOPage.getRecords();
        return Result.ok(productCategoryPageVOPage);
    }

    @Override
    public Result saveProductCategory(ProductCategory productCategory) {
        Long parentId = productCategory.getParentId();
        if(Objects.isNull(parentId)) {
           this.save(productCategory);
           return Result.ok();
        }
        //查询父类id
        Long count = this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getId, parentId));
        if(count < 1) {
            return Result.fail("父类id不存在");
        }
        this.save(productCategory);
        return Result.ok();
    }

    @Override
    public Result pageList() {
        //数据库中所有的分类
        List<ProductCategoryPageVO> list = productCategoryMapper.selectList();

        if(list.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
        //树形结构
        Map<Long, ProductCategoryPageVO> map = list.stream().collect(Collectors.toMap(ProductCategoryPageVO::getId, Function.identity()));
        List<ProductCategoryPageVO> roots = new ArrayList<>();

        for(ProductCategoryPageVO node : list) {
            Long parentId = node.getParentId();
            if(parentId == null) {
                roots.add(node);
            }else {

                ProductCategoryPageVO parent = map.get(parentId);
                if(parent != null) {
                    if(parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }
        log.info(roots.toString());
        return Result.ok(roots);
    }

    @Override
    public Result deleteProductCategory(Long id) {
        Long count = this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getId, id));
        if(count < 1) {
            return Result.fail("不存在该商品分类");
        }
        Long children = this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParentId, id));
        if(children > 0) {
            return Result.fail("存在子分类");
        }
        this.removeById(id);
        return Result.ok();
    }
}
