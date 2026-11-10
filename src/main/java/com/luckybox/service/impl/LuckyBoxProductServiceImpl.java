package com.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.MqConstants;
import com.luckybox.constant.RedisConstants;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.LuckyBoxMapper;
import com.luckybox.mapper.LuckyBoxOrderMapper;
import com.luckybox.mapper.LuckyBoxProductMapper;
import com.luckybox.mapper.ProductSkuMapper;
import com.luckybox.pojo.dto.NoticeMessage;
import com.luckybox.pojo.dto.PageDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.*;

import com.luckybox.pojo.vo.LuckyBoxProductItemVO;
import com.luckybox.pojo.vo.LuckyBoxProductsVO;
import com.luckybox.service.ILuckyBoxProductService;
import com.luckybox.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LuckyBoxProductServiceImpl extends ServiceImpl<LuckyBoxProductMapper, LuckyBoxProduct> implements ILuckyBoxProductService {

    private final LuckyBoxMapper luckyBoxMapper;
    private final LuckyBoxProductMapper luckyBoxProductMapper;
    private final INotificationService notificationService;
    private final ProductSkuMapper productSkuMapper;
    private final RedissonClient redissonClient;
    private final LuckyBoxOrderMapper luckyBoxOrderMapper;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    @Transactional
    public Result addLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct) {
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxProduct.getLuckyBoxId()));
        BigDecimal num = BigDecimal.ZERO;
        // 1. 判断是不是保底商品（库存为 null 代表无限/保底）
        boolean isFallback = (luckyBoxProduct.getStock() == null);

        // 2. 如果是保底商品，强制概率为 0，防止污染正常奖池的概率
        if (isFallback) {
            luckyBoxProduct.setProbability(BigDecimal.ZERO);
        }
        //判断是否有库存
        if(!isFallback) {
            boolean isEnough = productSkuMapper.deductStock(luckyBoxProduct.getProductSkuId(), luckyBoxProduct.getStock() == null ? 0 : luckyBoxProduct.getStock());
            if(!isEnough) {
                return Result.fail("库存不足");
            }
        }
        for(LuckyBoxProduct l : list) {
            num = num.add(l.getProbability());
        }
        if((num.add(luckyBoxProduct.getProbability()).compareTo(BigDecimal.ONE) > 0)) {
            return Result.fail("概率过大");
        }
        if((num.add(luckyBoxProduct.getProbability()).compareTo(BigDecimal.ONE) < 0)) {
            luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().eq(LuckyBox::getId, luckyBoxProduct.getLuckyBoxId()).set(LuckyBox::getStatus, SystemConstants.DISABLE));
        }
        boolean suc = this.save(luckyBoxProduct);
        if(!suc) {
            return Result.fail("添加失败");
        }
        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteLuckyBoxProduct(Long id) {

        LuckyBoxProduct boxProduct = this.getById(id);
        if(boxProduct == null) {
            return Result.fail("不存在");
        }
        //如果盲盒底下有订单就无法删除
        Long boxId = boxProduct.getLuckyBoxId();
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, boxId));
        boolean hasDefault = list.stream().anyMatch(i -> i.getStock() == null);
        //如果列表为空，暂时禁用，并通知管理员
        if(list.isEmpty() || !hasDefault) {
            NoticeMessage noticeMessage = new NoticeMessage();
            noticeMessage.setId(boxId);
            rabbitTemplate.convertAndSend(MqConstants.NOTICE_TOPIC_EXCHANGE, MqConstants.NOTICE_INSERT_KEY, noticeMessage);
            stringRedisTemplate.delete(RedisConstants.CACHE_BOX_KEY + boxId);
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }

    @Override
    public Result updateLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct) {

        //判断是否有库存

        //查当前盲盒的所有
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxProduct.getLuckyBoxId()));
        LuckyBoxProduct boxProduct = this.getById(luckyBoxProduct.getId());
        if(boxProduct == null) {
            return Result.fail("没有该规则");
        }
        BigDecimal total = list.stream().map(LuckyBoxProduct::getProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        if(total.subtract(boxProduct.getProbability()).add(luckyBoxProduct.getProbability()).compareTo(BigDecimal.ONE) > 0){
            return Result.fail("概率过大");
        }
        boolean suc = this.updateById(luckyBoxProduct);
       // 查一下目前数据库里还有没有保底商品（stock is null）
        Long nullCount = this.count(new LambdaQueryWrapper<LuckyBoxProduct>()
                .eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxProduct.getLuckyBoxId())
                .isNull(LuckyBoxProduct::getStock)
        );

        if (nullCount == 0) {
            // 无保底禁用
            luckyBoxMapper.update(null, new LambdaUpdateWrapper<LuckyBox>()
                    .eq(LuckyBox::getId, luckyBoxProduct.getLuckyBoxId())
                    .set(LuckyBox::getStatus, SystemConstants.DISABLE));
        }
        //概率<1禁用
        if(total.subtract(boxProduct.getProbability()).add(luckyBoxProduct.getProbability()).compareTo(BigDecimal.ONE) < 0) {
            luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().eq(LuckyBox::getId, luckyBoxProduct.getLuckyBoxId()).set(LuckyBox::getStatus, SystemConstants.DISABLE));
        }
        if(!suc) {
            return Result.fail("修改失败");
        }
        return Result.ok();
    }

    @Override
    public Result getLuckyBoxProducts() {
        //对所有盲盒查询
        Page<LuckyBox> boxPage = luckyBoxMapper.selectPage(new Page<>(SystemConstants.BOX_PAGE_NUM, SystemConstants.BOX_PAGE_SIZE), new LambdaQueryWrapper<>());
        if(boxPage.getRecords().isEmpty()) {
            PageDTO result = new PageDTO();
            result.setPages(0L);
            result.setTotal(boxPage.getTotal());
            result.setCurrent(boxPage.getCurrent());
            result.setSize(boxPage.getSize());
            result.setData(Collections.emptyList());
            return Result.ok(result);
        }
        Set<Long> boxIds = boxPage.getRecords().stream().map(LuckyBox::getId).collect(Collectors.toSet());
        List<LuckyBoxProductItemVO> list = luckyBoxProductMapper.getBoxProductList(boxIds);
        if(list == null || list.isEmpty()) {
            return Result.ok();
        }
        LuckyBoxProductsVO vos = new LuckyBoxProductsVO();
        //分组
        Map<Long, List<LuckyBoxProductItemVO>> map = list.stream().collect(Collectors.groupingBy(LuckyBoxProductItemVO::getLuckyBoxId));
        map.forEach((key, value) -> {
            vos.setLuckyBoxId(key);
            vos.setName(value.getFirst().getLuckyBoxName());
            vos.setList(value);
        });
        //组装
        List<LuckyBoxProductsVO> voList = boxPage.getRecords().stream()
                .map(box -> {
                    LuckyBoxProductsVO vo = new LuckyBoxProductsVO();
                    vo.setLuckyBoxId(box.getId());
                    vo.setName(box.getName());
                    vo.setList(map.getOrDefault(box.getId(), Collections.emptyList()));
                    return vo;
                })
                .toList();
        PageDTO dto = PageDTO.build(boxPage, voList);
        //树形结构返回vo
        return Result.ok(dto);
    }

    @Override
    @Transactional
    public Result batchAddLuckyBoxProduct(List<LuckyBoxProduct> luckyBoxProducts) {
        if(luckyBoxProducts == null || luckyBoxProducts.isEmpty()) {
            return Result.ok();
        }
        Long boxId = luckyBoxProducts.getFirst().getLuckyBoxId();
//        对 boxId 加锁（Redisson ）
        RLock lock = redissonClient.getLock(RedisConstants.BOX_ID_LOCK_KEY + boxId);
        lock.tryLock();
        boolean isLock;
        try {
            isLock = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (!isLock) {
                return Result.fail("系统繁忙，请稍后重试");
            }
            //查看盲盒id已经存在的列表
            List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, boxId));
            BigDecimal exists = list.stream().map(LuckyBoxProduct::getProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
            //概率是否过大
            BigDecimal total = luckyBoxProducts.stream().map(LuckyBoxProduct::getProbability).reduce(exists, BigDecimal::add);

            if(total.compareTo(BigDecimal.ONE) > 0) {
                return Result.fail("添加失败，概率过大");
            }
            if(total.compareTo(BigDecimal.ONE) < 0) {
                luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().eq(LuckyBox::getId, boxId).set(LuckyBox::getStatus, SystemConstants.DISABLE));
            }
            //判断添加商品的库存够不够(过滤保底商品)
            List<Long> skuIds = luckyBoxProducts.stream().filter(l -> l.getStock() != null).map(LuckyBoxProduct::getProductSkuId).toList();
            Map<Long, ProductSku> skuMap = productSkuMapper.selectBatchIds(skuIds).stream()
                    .collect(Collectors.toMap(ProductSku::getId, Function.identity()));
            for(LuckyBoxProduct item : luckyBoxProducts) {
                ProductSku sku = skuMap.get(item.getProductSkuId());
                if (sku == null) {
                    return Result.fail("商品SKU不存在: " + item.getProductSkuId());
                }
                // 判断真实库存是否够分给盲盒
                if (sku.getStock() - item.getStock() < 0) {
                    return Result.fail("商品SKU库存不足，ID: " + item.getProductSkuId() + "，剩余库存: " + sku.getStock());
                }

                if(total.compareTo(BigDecimal.ONE) < 0) {
                    luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().set(LuckyBox::getStatus, SystemConstants.DISABLE).eq(LuckyBox::getId, luckyBoxProducts.getFirst().getLuckyBoxId()));
                }
                }

            int c = productSkuMapper.updateBatch(luckyBoxProducts);
            if(c != luckyBoxProducts.size()) {
                return Result.fail("添加失败");
            }

            boolean suc = luckyBoxProductMapper.insertBatch(luckyBoxProducts);
                Long count = this.count(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxProducts.getFirst().getLuckyBoxId())
                        .isNull(LuckyBoxProduct::getStock));

                if(count == 0) {
                    // 无保底禁用
                    luckyBoxMapper.update(null, new LambdaUpdateWrapper<LuckyBox>()
                            .eq(LuckyBox::getId, luckyBoxProducts.getFirst().getLuckyBoxId())
                            .set(LuckyBox::getStatus, SystemConstants.DISABLE));
                }

                if(!suc) {
                    return Result.fail("添加失败");
                }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteLuckyBoxProducts(List<Long> ids) {
        Set<Long> boxIds= new HashSet<>();
        List<LuckyBoxProduct> luckyBoxProducts = this.listByIds(ids);
        if(luckyBoxProducts == null || luckyBoxProducts.isEmpty()) {
            return Result.fail("不存在");
        }
        //获取魔盒id
        boxIds =  luckyBoxProducts.stream().map(LuckyBoxProduct::getLuckyBoxId).collect(Collectors.toSet());
        Long count = luckyBoxOrderMapper.selectCount(new LambdaQueryWrapper<LuckyBoxOrder>().in(LuckyBoxOrder::getLuckyBoxId, boxIds));

        if(count > 0) {
            return Result.fail("盲盒有订单，无法删除");
        }
        //批量删除规则
        this.removeBatchByIds(ids);
        List<LuckyBoxProduct> list = luckyBoxProductMapper.getBatchByBoxIds(boxIds);
        // 先把剩余奖品按盲盒 ID 分组（注意：这里只包含还有奖品的盲盒）
        Map<Long, List<LuckyBoxProduct>> remainMap = list.stream()
                .collect(Collectors.groupingBy(LuckyBoxProduct::getLuckyBoxId));
        Set<Long> disableIds = new HashSet<>();
        for (Long boxId : boxIds) {
            // 获取该盲盒剩下的奖品（如果被彻底删空了，这里返回空集合）
            List<LuckyBoxProduct> remainProducts = remainMap.getOrDefault(boxId, Collections.emptyList());

            // 判断条件：如果没奖品了 || 剩下的里没保底
            boolean hasProducts = remainProducts.isEmpty();
            boolean hasFallback = remainProducts.stream().anyMatch(p -> p.getStock() == null);

            if (hasProducts || !hasFallback) {
                disableIds.add(boxId);
            }
        }
        if(!disableIds.isEmpty()) {
            luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().set(LuckyBox::getStatus, SystemConstants.DISABLE).in(LuckyBox::getId, disableIds));
            //发送消息队列
            NoticeMessage noticeMessage = new NoticeMessage();
            noticeMessage.setIds(disableIds);
            rabbitTemplate.convertAndSend(MqConstants.NOTICE_TOPIC_EXCHANGE, MqConstants.NOTICE_INSERT_KEY, noticeMessage);
            Set<String> boxCacheIds = disableIds.stream().map(i -> RedisConstants.CACHE_BOX_KEY + i).collect(Collectors.toSet());
            stringRedisTemplate.delete(boxCacheIds);
        }
        return Result.ok();
    }

    @Override
    public Result getLuckyBoxProduct(Long id) {
        LuckyBoxProductItemVO boxProduct = luckyBoxProductMapper.getBoxProduct(id);
        return Result.ok(boxProduct);
    }

    @Override
    public Result getLuckyBoxProductsByBoxIds(List<Long> boxIds) {
        //对id批量，分页
        Page<LuckyBox> boxPage = luckyBoxMapper.selectPage(new Page<>(SystemConstants.BOX_PAGE_NUM, SystemConstants.BOX_PAGE_SIZE),
                new LambdaQueryWrapper<LuckyBox>().in(LuckyBox::getId, boxIds));
        PageDTO dto = PageDTO.build(boxPage, Collections.emptyList());
        if(boxPage.getRecords().isEmpty()) {
            return Result.ok(dto);
        }
        List<LuckyBoxProductItemVO> boxProductList = luckyBoxProductMapper.getBoxProductList(new HashSet<>(boxIds));
        if(boxProductList == null || boxProductList.isEmpty()) {
            return Result.ok(dto);
        }
        //后分组
        Map<Long, List<LuckyBoxProductItemVO>> map = boxProductList.stream().collect(Collectors.groupingBy(LuckyBoxProductItemVO::getLuckyBoxId));
        //匹配boxPage
        List<LuckyBoxProductsVO> list = boxPage.getRecords().stream().map(box -> {
            LuckyBoxProductsVO vo = new LuckyBoxProductsVO();
            vo.setLuckyBoxId(box.getId());
            vo.setName(box.getName());
            vo.setList(map.get(box.getId()));
            return vo;
        }).toList();
        dto = PageDTO.build(boxPage, list);
        return Result.ok(dto);
    }
}
