package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.LuckyBoxCategorySelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxCategory;
import com.luckybox.service.ILuckyBoxCategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/box-category")
public class LuckyBoxCategoryController {
    @Resource
    private ILuckyBoxCategoryService luckyBoxCategoryService;
    @PostMapping
    @AdminRequired
    public Result create(@RequestBody LuckyBoxCategory luckyBoxCategory) {
        boolean suc = luckyBoxCategoryService.save(luckyBoxCategory);
        if(!suc) {
            return Result.fail("创建分类失败");
        }
        return Result.ok();
    }
    @PutMapping()
    @AdminRequired
    public Result update(@RequestBody LuckyBoxCategory luckyBoxCategory) {
        boolean suc = luckyBoxCategoryService.updateById(luckyBoxCategory);
        if(!suc) {
            return Result.fail("创建分类失败");
        }
        return Result.ok();
    }
    @DeleteMapping("/{id}")
    @AdminRequired
    public Result remove(@PathVariable Long id) {
        return luckyBoxCategoryService.deleteBoxCategory(id);
    }
    @PostMapping("/page")
    public Result list(@RequestBody LuckyBoxCategorySelectDTO luckyBoxCategorySelectDTO) {
        return luckyBoxCategoryService.getBoxCategoryList(luckyBoxCategorySelectDTO);
    }

}
