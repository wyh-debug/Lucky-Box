package com.luckybox.pojo.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    private String nickname;
    private String phone;
    private String code;
    private String password;
    private String role;
}
