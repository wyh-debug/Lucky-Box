package com.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.CardMapper;
import com.luckybox.mapper.LuckyBoxCardMapper;
import com.luckybox.mapper.LuckyBoxMapper;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Card;
import com.luckybox.pojo.entity.LuckyBox;
import com.luckybox.pojo.entity.LuckyBoxCard;
import com.luckybox.service.ILuckyBoxCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LuckyBoxCardServiceImpl extends ServiceImpl<LuckyBoxCardMapper, LuckyBoxCard> implements ILuckyBoxCardService {
    private final LuckyBoxMapper luckyBoxMapper;
    private final CardMapper cardMapper;
    @Override
    @Transactional
    public Result create(LuckyBoxCard luckyBoxCard) {
        if(luckyBoxCard.getProbabilityOverride().compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("概率大于1，添加失败");
        }
        LuckyBox luckyBox = luckyBoxMapper.selectById(luckyBoxCard.getLuckyBoxId());
        if(luckyBox == null) {
            return Result.fail("不含盲盒");
        }
        Card card = cardMapper.selectById(luckyBoxCard.getCardId());
        if(card == null) {
            return Result.fail("不含卡片");
        }
        if(luckyBoxCard.getProbabilityOverride() == null) {
            luckyBoxCard.setProbabilityOverride(card.getProbability());
        }
        //获取当前魔盒的所有卡片规则
        List<LuckyBoxCard> list = this.list(new LambdaQueryWrapper<LuckyBoxCard>().eq(LuckyBoxCard::getLuckyBoxId, luckyBoxCard.getLuckyBoxId()));
        BigDecimal total = list.stream().map(LuckyBoxCard::getProbabilityOverride).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(total.add(luckyBoxCard.getProbabilityOverride()).compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("概率过大，添加失败");
        }
        if(total.add(luckyBoxCard.getProbabilityOverride()).compareTo(BigDecimal.ONE) < 0) {
            //小于0禁用，不能抽奖
            luckyBox.setStatus(SystemConstants.DISABLE);
            luckyBoxMapper.updateById(luckyBox);
        }
        this.save(luckyBoxCard);
        return Result.ok();
    }

    @Transactional
    @Override
    public Result createBatch(List<LuckyBoxCard> list) {
        LuckyBox luckyBox = luckyBoxMapper.selectById(list.getFirst().getLuckyBoxId());
        if(luckyBox == null) {
            return Result.fail("盲盒不存在");
        }
        Set<Long> cardIds = list.stream().map(LuckyBoxCard::getCardId).collect(Collectors.toSet());
        List<Card> cards = cardMapper.selectBatchIds(cardIds);
        if(cards.size() < cardIds.size()) {
            return Result.fail("卡片不存在");
        }
        Map<Long, List<Card>> collect = cards.stream().collect(Collectors.groupingBy(Card::getId));

        //重新设置概率
        List<LuckyBoxCard> luckyBoxCards = list.stream().map(i -> {
            return i.getProbabilityOverride() == null ? i.setProbabilityOverride(collect.get(i.getCardId()).getFirst().getProbability()) : i.setProbabilityOverride(i.getProbabilityOverride());
        }).toList();
        //新添加的total
        BigDecimal addTotal = luckyBoxCards.stream().map(LuckyBoxCard::getProbabilityOverride).reduce(BigDecimal.ZERO, BigDecimal::add);
        //原本的
        List<LuckyBoxCard> oriLuckyBoxCards = this.list(new LambdaQueryWrapper<LuckyBoxCard>().eq(LuckyBoxCard::getLuckyBoxId, luckyBox.getId()));
        BigDecimal total = oriLuckyBoxCards.stream().map(LuckyBoxCard::getProbabilityOverride).reduce(addTotal, BigDecimal::add);
        if(total.compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("概率过大");
        }
        if(total.compareTo(BigDecimal.ONE) < 0) {
            //不满1无法抽奖，禁用盲盒
            luckyBox.setStatus(SystemConstants.DISABLE);
            luckyBoxMapper.updateById(luckyBox);
        }
        this.saveBatch(list);
        return Result.ok();
    }
}
