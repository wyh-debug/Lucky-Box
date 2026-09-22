package com.luckybox.pojo.vo;

import com.luckybox.pojo.entity.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddressVO {
    Long topId;
    List<Address> list;
}
