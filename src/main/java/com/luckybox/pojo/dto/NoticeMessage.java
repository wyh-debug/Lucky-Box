package com.luckybox.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class NoticeMessage {
    //单个消息
    private Long id;
    //多个要删除的id
    private Set<Long> ids;
}
