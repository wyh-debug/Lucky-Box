package com.luckybox.controller;

import com.luckybox.service.ILuckyBoxOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/box-order")
public class LuckyBoxOrderController {
    @Resource
    private ILuckyBoxOrderService luckyBoxOrderService;

}

