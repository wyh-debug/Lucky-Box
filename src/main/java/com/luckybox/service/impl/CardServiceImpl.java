package com.luckybox.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.mapper.CardMapper;
import com.luckybox.mapper.CardTypeMapper;
import com.luckybox.mapper.LuckyBoxCardMapper;
import com.luckybox.pojo.dto.CardDTO;
import com.luckybox.pojo.dto.CardSelectDTO;
import com.luckybox.pojo.dto.PageDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Card;
import com.luckybox.pojo.entity.CardType;
import com.luckybox.service.ICardService;
import com.luckybox.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {
    private final CardTypeMapper cardTypeMapper;
    private final LuckyBoxCardMapper luckyBoxCardMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final CardMapper cardMapper;
    private final CacheClient cacheClient;
    @Override
    public Result createCard(Card card) {
        if(card.getProbability().compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("概率不能大于1");
        }
        CardType cardType = cardTypeMapper.selectById(card.getCardTypeId());
        if(cardType == null) {
            return Result.fail("无该分类");
        }
        this.save(card);
        return Result.ok();
    }

    @Override
    public Result delete(Long id) {
        boolean isExist = luckyBoxCardMapper.existCard(id);
        if(isExist) {
            return Result.fail("不可删除");
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        stringRedisTemplate.delete(RedisConstants.CACHE_CARD_KEY + id);
        return Result.ok();
    }

    @Override
    public Result updateCard(Card card) {
        if(card.getProbability().compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("概率不能大于1");
        }
        CardType cardType = cardTypeMapper.selectById(card.getCardTypeId());
        if(cardType == null) {
            return Result.fail("无类型");
        }
        boolean suc = this.updateById(card);
        if(!suc) {
            return Result.fail("删除失败");
        }
        stringRedisTemplate.delete(RedisConstants.CACHE_CARD_KEY + card.getId());

        return Result.ok();
    }

    @Override
    public Result pageQuery(CardSelectDTO cardSelectDTO) {
        Page<Card> cardPage = cardMapper.selectCardList(cardSelectDTO.getPageQuery(), cardSelectDTO);
        PageDTO dto = PageDTO.build(cardPage, cardPage.getRecords());
        return Result.ok(dto);
    }

    @Override
    public Result getCard(Long id) {
        CardDTO cardDTO = cacheClient.cacheNullValue(RedisConstants.LOCK_CARD_KEY, id, CardDTO.class, RedisConstants.CACHE_CARD_TTL, TimeUnit.MINUTES,
                cardMapper::getCard);
        if(cardDTO == null) {
            return Result.ok();
        }
        return Result.ok(cardDTO);
    }
}
