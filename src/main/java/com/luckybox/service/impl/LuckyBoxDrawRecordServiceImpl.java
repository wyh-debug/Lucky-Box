package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.LuckyBoxDrawRecordMapper;
import com.luckybox.mapper.LuckyBoxProductMapper;
import com.luckybox.mapper.ProductMapper;
import com.luckybox.mapper.ProductSkuMapper;
import com.luckybox.pojo.dto.ProductSnapshotDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxDrawRecord;
import com.luckybox.pojo.entity.LuckyBoxProduct;
import com.luckybox.pojo.entity.Product;
import com.luckybox.pojo.entity.ProductSku;
import com.luckybox.service.ILuckyBoxDrawRecordService;
import com.luckybox.service.IProductOrderService;
import com.luckybox.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LuckyBoxDrawRecordServiceImpl extends ServiceImpl<LuckyBoxDrawRecordMapper, LuckyBoxDrawRecord> implements ILuckyBoxDrawRecordService {
    private final LuckyBoxProductMapper luckyBoxProductMapper;
    private final CacheClient cacheClient;
    private final ProductSkuMapper productSkuMapper;
    private final ProductMapper productMapper;
    private final IProductOrderService productOrderService;
    @Override
    @Transactional
    public Result create(Long id) {
//        盲盒抽奖
//        查询盲盒规则（status = 0）
        LuckyBoxDrawRecord luckyBoxDrawRecord = this.getOne(new LambdaQueryWrapper<LuckyBoxDrawRecord>().eq(LuckyBoxDrawRecord::getId, id).eq(LuckyBoxDrawRecord::getStatus, SystemConstants.ENABLE));
//        根据概率抽商品
//        获取规则列表
        List<LuckyBoxProduct> list = luckyBoxProductMapper.selectList(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxDrawRecord.getLuckyBoxId()).eq(LuckyBoxProduct::getStatus, SystemConstants.ENABLE));
        List<LuckyBoxProduct> luckyBoxProducts = list.stream().filter(l -> {
            if (l.getStock() == 0) {
                l.setStatus(SystemConstants.DISABLE);
                return false;
            }
            return true;
        }).toList();
        if(luckyBoxProducts.isEmpty()) {
            return Result.fail("无法抽奖，请申请退款");
        }
        //概率归1
        BigDecimal total = luckyBoxProducts.stream().map(LuckyBoxProduct::getProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal calculation = BigDecimal.ZERO;
        BigDecimal random = RandomUtil.randomBigDecimal(BigDecimal.ZERO, total);
        LuckyBoxProduct winner = null;
        for(LuckyBoxProduct l : luckyBoxProducts) {
            calculation = calculation.add(l.getProbability());
            if(random.compareTo(calculation) < 0) {
                winner = l;
                break;
            }
        }
        if(winner == null) {
            winner = luckyBoxProducts.getLast();
        }
        ProductSku productSku = productSkuMapper.selectById(winner.getProductSkuId());
        if(productSku == null) {
            Result.fail("商品不存在");
        }
        //        减少对应库存(原子减库存)
        boolean suc = luckyBoxProductMapper.deductStock(id, 1);
        if(!suc) {
            Result.fail("库存不足");
        }
//        生成快照
//        缓存获取product
        Product product = cacheClient.handleCacheBreakDown(RedisConstants.CACHE_PRODUCT_KEY, productSku.getProductId(), Product.class, productMapper::selectById);
        log.info(product.toString());
        ProductSnapshotDTO productSnapshotDTO = BeanUtil.copyProperties(productSku, ProductSnapshotDTO.class);
        productSnapshotDTO.setName(product.getName());
        productSnapshotDTO.setBrand(product.getBrand());
        productSnapshotDTO.setAttributes(product.getAttributes());
        productSnapshotDTO.setQualityType(product.getQualityType());
        log.info(productSnapshotDTO.toString());
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(productSnapshotDTO);
        log.info(stringObjectMap.toString());
        luckyBoxDrawRecord.setWinningProductSkuId(winner.getProductSkuId());
        luckyBoxDrawRecord.setWinningProductSnapshot(stringObjectMap);
        luckyBoxDrawRecord.setStatus(SystemConstants.DISABLE);

        //关联发货单，创建一下订单
        Long orderId = productOrderService.addOrder(luckyBoxDrawRecord.getId(), winner.getProductSkuId(), luckyBoxDrawRecord.getAddressId(), 1);
        luckyBoxDrawRecord.setProductOrderId(orderId);
        boolean suc2 = this.updateById(luckyBoxDrawRecord);
        if(!suc2) {
            return Result.fail("添加失败");
        }
        return Result.ok(orderId);
    }
}
