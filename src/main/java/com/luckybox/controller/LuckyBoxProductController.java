package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.service.ILuckyBoxProductService;
import jakarta.annotation.Resource;
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
    @GetMapping()
    @AdminRequired
    public Result getList() {
        return luckyBoxProductService.getLuckyBoxProducts();
    }

}
