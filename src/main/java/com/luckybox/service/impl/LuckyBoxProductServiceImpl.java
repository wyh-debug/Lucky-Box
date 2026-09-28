package com.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.LuckyBoxMapper;
import com.luckybox.mapper.LuckyBoxProductMapper;
import com.luckybox.pojo.dto.NotificationDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBox;
import com.luckybox.pojo.entity.LuckyBoxProduct;

import com.luckybox.service.ILuckyBoxProductService;
import com.luckybox.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LuckyBoxProductServiceImpl extends ServiceImpl<LuckyBoxProductMapper, LuckyBoxProduct> implements ILuckyBoxProductService {

    private final LuckyBoxMapper luckyBoxMapper;
    private final LuckyBoxProductMapper luckyBoxProductMapper;
    private final INotificationService notificationService;
    @Override
    public Result addLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct) {
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, luckyBoxProduct.getLuckyBoxId()));
        BigDecimal num = BigDecimal.ZERO;
        for(LuckyBoxProduct l : list) {
            num = num.add(l.getProbability());
        }
        if((num.add(luckyBoxProduct.getProbability()).compareTo(BigDecimal.ONE) > 0)) {
            return Result.fail("概率过大");
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
        Long boxId = boxProduct.getLuckyBoxId();
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, boxId));
        //如果列表为空，暂时禁用，并通知管理员
        if(list.size() <= 1) {
            luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().set(LuckyBox::getStatus, SystemConstants.DISABLE).eq(LuckyBox::getId, boxId));
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setTitle("盲盒规则为空通知");
            notificationDTO.setType(SystemConstants.NOTIFICATION_TYPE_BOX);
            notificationDTO.setContent("盲盒：" + boxId + "关联规则为空");
            notificationDTO.setBizId(boxId);
            notificationService.sendToAdmin(notificationDTO);
        }
        boolean suc = this.removeById(id);
        if(!suc) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }

    @Override
    public Result updateLuckyBoxProduct(LuckyBoxProduct luckyBoxProduct) {
        return null;
    }

    @Override
    public Result getLuckyBoxProducts() {
        return null;
    }

    @Override
    public Result batchAddLuckyBoxProduct(List<LuckyBoxProduct> luckyBoxProducts) {
        if(luckyBoxProducts == null && luckyBoxProducts.isEmpty()) {
            return Result.ok();
        }
        Long boxId = luckyBoxProducts.get(0).getLuckyBoxId();
        List<LuckyBoxProduct> list = this.list(new LambdaQueryWrapper<LuckyBoxProduct>().eq(LuckyBoxProduct::getLuckyBoxId, boxId));
        BigDecimal exists = list.stream().map(LuckyBoxProduct::getProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        //概率是否过大
        BigDecimal total = luckyBoxProducts.stream().map(LuckyBoxProduct::getProbability).reduce(exists, BigDecimal::add);
        if(total.compareTo(BigDecimal.ONE) > 0) {
            return Result.fail("添加失败，概率过大");
        }
        boolean suc = luckyBoxProductMapper.insertBatch(luckyBoxProducts);
        if(!suc) {
            return Result.fail("添加失败");
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
        //批量删除规则
        this.removeBatchByIds(ids);
        List<LuckyBoxProduct> list = luckyBoxProductMapper.getBatchByBoxIds(boxIds);
        //全部都没有
        if(list == null || list.isEmpty()) {
            //批量禁用
            luckyBoxMapper.updateBatchByIds(boxIds);

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setTitle("盲盒规则为空通知");
            notificationDTO.setType(SystemConstants.NOTIFICATION_TYPE_BOX);
            //在内容里添加 关联 业务id
            notificationDTO.setContent("盲盒：" + boxIds + "关联规则为空");
            notificationService.sendToAdmin(notificationDTO);
            return Result.ok();
        }
        //部分没有
        Map<Long, List<LuckyBoxProduct>> collect = list.stream().collect(Collectors.groupingBy(LuckyBoxProduct::getLuckyBoxId));
        Iterator<Map.Entry<Long, List<LuckyBoxProduct>>> iterator = collect.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<LuckyBoxProduct>> entry = iterator.next();
            if(entry.getValue().isEmpty()) {
                luckyBoxMapper.update(new LambdaUpdateWrapper<LuckyBox>().set(LuckyBox::getStatus, SystemConstants.DISABLE).eq(LuckyBox::getId, entry.getKey()));
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setContent("盲盒：" + boxIds + "关联规则为空");
                notificationDTO.setType(SystemConstants.NOTIFICATION_TYPE_BOX);
                notificationService.sendToAdmin(notificationDTO);
            }
        }
        return Result.ok();
    }
}
