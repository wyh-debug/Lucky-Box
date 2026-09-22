package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.LuckyBoxCategoryMapper;
import com.luckybox.mapper.LuckyBoxMapper;
import com.luckybox.pojo.dto.LuckyBoxCategorySelectDTO;
import com.luckybox.pojo.dto.PageDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxCategory;
import com.luckybox.service.ILuckyBoxCategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LuckyBoxCategoryServiceImpl extends ServiceImpl<LuckyBoxCategoryMapper, LuckyBoxCategory> implements ILuckyBoxCategoryService {
    @Resource
    private LuckyBoxMapper luckyBoxMapper;
    @Resource
    private LuckyBoxCategoryMapper luckyBoxCategoryMapper;
    @Override
    public Result deleteBoxCategory(Long id) {
        boolean exists = luckyBoxMapper.existsByCategoryId(id);
        if(exists) {
            return Result.fail("有盒子，删除失败");
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }

    @Override
    public Result getBoxCategoryList(LuckyBoxCategorySelectDTO luckyBoxCategorySelectDTO) {
        Page<LuckyBoxCategory> page = (Page<LuckyBoxCategory>) luckyBoxCategorySelectDTO.getPageQuery();
//        Page<LuckyBoxCategory> categories = this.page(page, new LambdaQueryWrapper<LuckyBoxCategory>()
//                .eq(LuckyBoxCategory::getId, luckyBoxCategorySelectDTO.getId())
//                .like(LuckyBoxCategory::getName, luckyBoxCategorySelectDTO.getName()));
//        PageDTO dto = PageDTO.build(categories, LuckyBoxCategory.class);
        page = luckyBoxCategoryMapper.selectCategoryPage(page, luckyBoxCategorySelectDTO);
        PageDTO dto = PageDTO.build(page, LuckyBoxCategory.class);
        return Result.ok(dto);
    }
}
