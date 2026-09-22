package com.luckybox.controller;

import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.service.IProductOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-order")
public class ProductOrderController {
    @Resource
    private IProductOrderService productOrderService;
    @PostMapping
    public Result create(@RequestBody OrderDTO orderDTO) {
        return productOrderService.addProductOrder(orderDTO);
    }

}
