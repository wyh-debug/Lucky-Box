package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Address;

public interface IAddressService extends IService<Address> {

    Result addAddress(Address address);

    Result getAddressList(Long userId);
}
