package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.ProductSku;
import com.luckybox.service.IProductSkuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {
    @Resource
    private IProductSkuService productSkuService;

    @AdminRequired
    @PostMapping
    public Result addProductSku(@RequestBody ProductSku productSku) {
        return productSkuService.addProductSKu(productSku);
    }
    @AdminRequired
    @DeleteMapping("/{id}")
    public Result deleteProductSku(@PathVariable("id") Long id) {
        return productSkuService.deleteProductSku(id);
    }

    @AdminRequired
    @PutMapping()
    public Result updateProductSku(@RequestBody ProductSku productSku) {
        return productSkuService.updateProductSku(productSku);
    }

    /**
     * 查询列表进入
     * @param productId
     * @return
     */
    @GetMapping("/pro/{proId}")
    public Result selectByProductId(@PathVariable("proId") Long productId) throws InterruptedException {
        return productSkuService.selectByProductId(productId);
    }

    /**
     * 从查询列表进入后点击其他sku
     * @param productSkuId
     * @return
     */
    @GetMapping("/{id}")
    public Result getProductSku(@PathVariable("id") Long productSkuId) throws InterruptedException {
        return productSkuService.selectByProductSkuId(productSkuId);
    }

}
