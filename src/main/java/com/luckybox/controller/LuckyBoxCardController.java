package com.luckybox.controller;

import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxCard;
import com.luckybox.service.ILuckyBoxCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/box-card")
@RequiredArgsConstructor
public class LuckyBoxCardController {
    private final ILuckyBoxCardService luckyBoxCardService;

    /**
     * 单独添加
     * @param luckyBoxCard
     * @return
     */
    @PostMapping
    public Result create(@RequestBody LuckyBoxCard luckyBoxCard) {
        return luckyBoxCardService.create(luckyBoxCard);
    }

    /**
     * 给1个盲盒添加
     * @param list
     * @return
     */
    @PostMapping("/batch")
    public Result createBatch(@RequestBody List<LuckyBoxCard> list) {
        return luckyBoxCardService.createBatch(list);
    }
}
