package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.service.ILuckyBoxProductService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/box-product")
public class LuckyBoxProductController {
    @Resource
    private ILuckyBoxProductService luckyBoxProductService;

    @PostMapping
    @AdminRequired
    public Result create(@RequestBody LuckyBoxProduct luckyBoxProduct) {
        return luckyBoxProductService.addLuckyBoxProduct(luckyBoxProduct);
    }
    @PostMapping("/{boxId}/products")
    @AdminRequired
    public Result batchAdd(@PathVariable("boxId") Long boxId,
            @RequestBody List<LuckyBoxProduct> luckyBoxProducts) {
        return luckyBoxProductService.batchAddLuckyBoxProduct(luckyBoxProducts);
    }
    @DeleteMapping("/{id}")
    @AdminRequired
    public Result remove(@PathVariable("id") Long id) {
        return luckyBoxProductService.deleteLuckyBoxProduct(id);
    }
    @DeleteMapping("/batch")
    @AdminRequired
    public Result batchRemove(@RequestBody List<Long> ids) {
        return luckyBoxProductService.deleteLuckyBoxProducts(ids);
    }
    @PutMapping
    @AdminRequired
    public Result update(@RequestBody LuckyBoxProduct luckyBoxProduct) {
        return luckyBoxProductService.updateLuckyBoxProduct(luckyBoxProduct);
    }
    @GetMapping("/list")
    public Result getList() {
        return luckyBoxProductService.getLuckyBoxProducts();
    }
    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Long id) {
        return luckyBoxProductService.getLuckyBoxProduct(id);
    }

    /**
     * 根据boxId获取列表批量
     * @param boxIds
     * @return
     */
    @GetMapping("/boxIds/box-products")
    public Result getProducts(@RequestBody List<Long> boxIds) {
        return luckyBoxProductService.getLuckyBoxProductsByBoxIds(boxIds);
    }

}
