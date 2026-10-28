package com.luckybox.controller;

import com.luckybox.annotation.AdminRequired;
import com.luckybox.pojo.dto.CardTypeSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.CardType;
import com.luckybox.service.ICardTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-type")
@RequiredArgsConstructor
public class CardTypeController {
    private final ICardTypeService cardTypeService;
    @PostMapping
    @AdminRequired
    public Result create(@RequestBody CardType cardType) {
        boolean suc = false;
        try {
            suc = cardTypeService.save(cardType);
        } catch (Exception e) {
            Result.fail("不可重复添加，添加失败");
        }
        if(!suc) {
            return Result.fail("添加失败");
        }
        return Result.ok();
    }
    @PutMapping
    @AdminRequired
    public Result update(@RequestBody CardType cardType) {
        boolean suc = cardTypeService.updateById(cardType);
        if(!suc) {
            return Result.fail("修改失败");
        }
        return Result.ok();
    }

    @PostMapping("/list")
    public Result pageQuery(@RequestBody CardTypeSelectDTO cardTypeSelectDTO) {
        return cardTypeService.pageQuery(cardTypeSelectDTO);
    }

    @DeleteMapping("/{id}")
    @AdminRequired
    public Result remove(@PathVariable("id") Long id) {
        return cardTypeService.removeCardType(id);
    }

}
