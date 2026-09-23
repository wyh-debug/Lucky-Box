package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.LuckyBoxSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBox;
import com.luckybox.service.ILuckyBoxService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/box")
public class LuckyBoxController {
    @Resource
    private ILuckyBoxService luckyBoxService;

    @AdminRequired
    @PostMapping
    public Result create(@RequestBody LuckyBox luckyBox) {
        return luckyBoxService.addLuckyBox(luckyBox);
    }
    @AdminRequired
    @DeleteMapping("/{id}")
    public Result remove(@PathVariable("id") Long id) {
        return luckyBoxService.deleteLuckyBox(id);
    }
    @AdminRequired
    @PutMapping()
    public Result update(@RequestBody LuckyBox luckyBox) {
        return luckyBoxService.updateLuckyBox(luckyBox);
    }

    @PostMapping("/page")
    public Result list(@RequestBody LuckyBoxSelectDTO luckyBoxSelectDTO) {
        return luckyBoxService.getLuckyBoxList(luckyBoxSelectDTO);
    }

    /**
     * 根据id获取，查缓存
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        return luckyBoxService.getLuckyBox(id);
    }

}
