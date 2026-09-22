package com.luckybox.controller;

import com.luckybox.service.IAddressService;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Address;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Resource
    private IAddressService addressService;
    @PostMapping()
    public Result create(@RequestBody Address address) {
        return addressService.addAddress(address);
    }

    /**
     * 根据userId获取地址列表
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}/addresses")
    public Result list(@PathVariable("userId") Long userId) {
        return addressService.getAddressList(userId);
    }

}
