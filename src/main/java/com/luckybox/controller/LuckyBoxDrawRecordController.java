package com.luckybox.controller;

import com.luckybox.pojo.dto.Result;
import com.luckybox.service.ILuckyBoxDrawRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draw-record")
@RequiredArgsConstructor
public class LuckyBoxDrawRecordController {
    private final ILuckyBoxDrawRecordService luckyBoxDrawRecordService;

    /**
     * 根据记录id抽奖
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Result draw(@PathVariable("id") Long id) {
        return luckyBoxDrawRecordService.create(id);
    }

}
