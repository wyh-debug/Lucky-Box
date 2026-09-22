package com.luckybox.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.ProductCategorySelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.ProductCategory;
import com.luckybox.service.IProductCategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {
    @Resource
    private IProductCategoryService productCategoryService;

    @PostMapping("/page")
    public Result queryPage(@RequestBody ProductCategorySelectDTO productCategorySelectDTO) {
        return productCategoryService.pageProductCategory(productCategorySelectDTO);
    }

    /**
     * 分级显示所有分类
     * @return
     */
    @GetMapping("/list" )
    public Result list() {
        return productCategoryService.pageList();
    }
    /**
     * 只允许管理员
     * @param productCategory
     * @return
     */
    @AdminRequired
    @PostMapping("/add")
    public Result queryPage(@RequestBody ProductCategory productCategory) {
        return productCategoryService.saveProductCategory(productCategory);
    }

    /**
     * 只允许管理员
     * @param productCategory
     * @return
     */
    @AdminRequired
    @PutMapping("/update")
    public Result update(@RequestBody ProductCategory productCategory) {
        productCategoryService.update(productCategory, new LambdaUpdateWrapper<ProductCategory>().eq(ProductCategory::getId, productCategory.getId()));
        return Result.ok();
    }

    /**
     * 只允许管理员
     * @param id
     * @return
     */
    @AdminRequired
    @DeleteMapping("/delete/{id}")
    public Result deleteProductCategory(@PathVariable("id") Long id) {
        return productCategoryService.deleteProductCategory(id);
    }


}
