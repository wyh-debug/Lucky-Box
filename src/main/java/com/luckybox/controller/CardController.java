package com.luckybox.controller;

import com.luckybox.pojo.dto.CardSelectDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Card;
import com.luckybox.service.ICardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final ICardService cardService;
    @PostMapping
    public Result create(@RequestBody Card card) {
        return cardService.createCard(card);
    };

    @DeleteMapping("/{id}")
    public Result remove(@PathVariable("id") Long id) {
        return cardService.delete(id);
    }

    @PutMapping
    public Result update(@RequestBody Card card) {
        return cardService.updateCard(card);
    }

    @PostMapping("/list")
    public Result pageQuery(@RequestBody CardSelectDTO cardSelectDTO) {
        return cardService.pageQuery(cardSelectDTO);
    }
    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Long id) {
        return cardService.getCard(id);
    }
}
