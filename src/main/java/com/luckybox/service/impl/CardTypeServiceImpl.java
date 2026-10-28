package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.CardMapper;
import com.luckybox.mapper.CardTypeMapper;
import com.luckybox.pojo.dto.CardTypeSelectDTO;
import com.luckybox.pojo.dto.PageDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.CardType;
import com.luckybox.service.ICardTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardTypeServiceImpl extends ServiceImpl<CardTypeMapper, CardType> implements ICardTypeService {
    private final CardTypeMapper cardTypeMapper;
    private final CardMapper cardMapper;
    @Override
    public Result pageQuery(CardTypeSelectDTO cardTypeSelectDTO) {
        Page<CardType> cardTypes = cardTypeMapper.selectCardTypes(cardTypeSelectDTO.getPageQuery(), cardTypeSelectDTO);
        PageDTO dto = PageDTO.build(cardTypes, cardTypes.getRecords());
        return Result.ok(dto);
    }

    @Override
    public Result removeCardType(Long id) {
        //检查是否存在卡片
        boolean isExist = cardMapper.existsCard(id);
        if(isExist) {
            return Result.fail("存在卡片，不可删除");
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }
}
