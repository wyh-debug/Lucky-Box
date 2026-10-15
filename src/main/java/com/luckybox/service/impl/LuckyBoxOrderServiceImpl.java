package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.*;
import com.luckybox.pojo.dto.*;
import com.luckybox.pojo.entity.*;
import com.luckybox.service.ILuckyBoxOrderService;
import com.luckybox.utils.CacheClient;
import com.luckybox.utils.OrderIdGenerate;
import com.luckybox.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LuckyBoxOrderServiceImpl extends ServiceImpl<LuckyBoxOrderMapper, LuckyBoxOrder> implements ILuckyBoxOrderService {
    private final OrderIdGenerate orderIdGenerate;
    private final LuckyBoxMapper luckyBoxMapper;
    private final AddressMapper addressMapper;
    private final BaseOrderMapper baseOrderMapper;
    private final LuckyBoxOrderMapper luckyBoxOrderMapper;
    private final LuckyBoxDrawRecordMapper luckyBoxDrawRecordMapper;
    @Override
    @Transactional
    public Result addBoxOrder(OrderDTO orderDTO) {

        Long boxId = orderDTO.getId();
        LuckyBox luckyBox = luckyBoxMapper.selectBox(boxId);
        if(luckyBox == null) {
            return Result.fail("没有盲盒信息");
        }
//        查看库存
        if (luckyBox.getTotalStock() - orderDTO.getCount() < 0) {
            return Result.fail("库存不足");
        }
//        改订单类型
        long orderId = orderIdGenerate.nextId("order");
//        查询盲盒信息，生成快照
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setId(orderId);
        baseOrder.setType(SystemConstants.ORDER_TYPE_BOX);
        Address address = addressMapper.selectById(orderDTO.getAddressId());
        AddressDTO dto = BeanUtil.copyProperties(address, AddressDTO.class);
        baseOrder.setAddress(JSONUtil.toJsonStr(dto));
        baseOrderMapper.insert(baseOrder);
        LuckyBoxOrder luckyBoxOrder = new LuckyBoxOrder();
        luckyBoxOrder.setId(orderId);
        luckyBoxOrder.setLuckyBoxId(orderDTO.getId());
        luckyBoxOrder.setLuckyBoxSnapshot(JSONUtil.toJsonStr(BeanUtil.copyProperties(luckyBox, LuckyBoxDTO.class)));
        luckyBoxOrder.setLuckyBoxCount(orderDTO.getCount());
        luckyBoxOrderMapper.insert(luckyBoxOrder);
//        批量添加抽奖记录
        List<LuckyBoxDrawRecord> luckyBoxDrawRecordList = new ArrayList<>();
        for(int i = 0; i < orderDTO.getCount(); i++) {
            LuckyBoxDrawRecord luckyBoxDrawRecord = new LuckyBoxDrawRecord();
            luckyBoxDrawRecord.setBaseOrderId(orderId);
            luckyBoxDrawRecord.setLuckyBoxId(orderDTO.getId());
            luckyBoxDrawRecord.setDrawIndex(i + 1);
            luckyBoxDrawRecord.setAddressId(orderDTO.getAddressId());
            luckyBoxDrawRecordList.add(luckyBoxDrawRecord);
        }
        luckyBoxDrawRecordMapper.insertBatch(luckyBoxDrawRecordList);
        return Result.ok(orderId);
    }
}
