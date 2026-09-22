package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.entity.User;
import com.luckybox.pojo.dto.LoginFormDTO;
import com.luckybox.pojo.dto.Result;

public interface IAuthService extends IService<User> {
    Result sendCode(String phone);

    Result login(LoginFormDTO loginFormDTO);

    Result adminLogin(LoginFormDTO loginFormDTO);
}
