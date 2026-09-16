package com.wyh.luckybox.controller;

import com.wyh.luckybox.annotation.AdminRequired;
import com.wyh.luckybox.pojo.dto.ProductSelectDTO;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.Product;
import com.wyh.luckybox.service.IProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private IProductService productService;

    @AdminRequired
    @PostMapping("/add")
    public Result addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @AdminRequired
    @PutMapping
    public Result updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/{id}")
    public Result getProduct(@PathVariable("id") Long id) throws InterruptedException {
        return productService.getProduct(id);
    }

    @PostMapping("/list")
    public Result getProductList(@RequestBody ProductSelectDTO productSelectDTO) {
        return productService.getProductList(productSelectDTO);
    }

    @AdminRequired
    @DeleteMapping("/{id}")
    public Result deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 按照定义的方式排序
     * @param sort
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/sort")
    public Result sortProduct(@RequestParam Integer sort, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return productService.sortProduct(sort, pageNum, pageSize);
    }
}
