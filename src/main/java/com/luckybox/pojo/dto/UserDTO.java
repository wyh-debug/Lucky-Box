package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nickname;
    private String avatar;
    private String role;
}
