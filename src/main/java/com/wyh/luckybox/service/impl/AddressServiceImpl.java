package com.wyh.luckybox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.luckybox.constant.SystemConstants;
import com.wyh.luckybox.mapper.AddressMapper;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.Address;
import com.wyh.luckybox.pojo.vo.AddressVO;
import com.wyh.luckybox.service.IAddressService;
import com.wyh.luckybox.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    @Transactional
    public Result addAddress(Address address) {
        Long userId = UserHolder.getUser().getId();
        if(address.getIsTop() == SystemConstants.ADDRESS_IS_TOP) {
            this.update(new LambdaUpdateWrapper<Address>().eq(Address::getUserId, userId).eq(Address::getIsTop, SystemConstants.ADDRESS_IS_TOP) .set(Address::getIsTop, SystemConstants.ADDRESS_NOT_TOP));
        }
        boolean success = this.save(address);
        if(!success) {
            return Result.fail("添加地址失败");
        }
        return Result.ok();

    }

    @Override
    public Result getAddressList(Long userId) {
        AddressVO addressVO = new AddressVO();
        List<Address> addresses;
        addresses = this.list(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId).orderByDesc(Address::getIsTop));
        if(addresses ==  null) {
            addresses = Collections.emptyList();
        }
        addressVO.setList(addresses);
        for(Address a : addresses) {
            if(a.getIsTop() == SystemConstants.ADDRESS_IS_TOP) {
                addressVO.setTopId(a.getId());
                break;
            }
        }
        return Result.ok(addressVO);
    }
}
