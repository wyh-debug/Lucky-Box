package com.luckybox.controller;

import com.luckybox.pojo.dto.OrderDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.service.ILuckyBoxOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/box-order")
public class LuckyBoxOrderController {
    @Resource
    private ILuckyBoxOrderService luckyBoxOrderService;

    @PostMapping
    public Result create(@RequestBody OrderDTO orderDTO) {
        return luckyBoxOrderService.addBoxOrder(orderDTO);
    }



}

