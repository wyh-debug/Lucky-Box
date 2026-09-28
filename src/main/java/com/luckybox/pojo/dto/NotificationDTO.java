package com.luckybox.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private String title;
    private String content;
    private Integer type;
    private Long bizId;
}
