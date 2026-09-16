package com.wyh.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.luckybox.pojo.dto.LoginFormDTO;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.entity.User;

public interface IAuthService extends IService<User> {
    Result sendCode(String phone);

    Result login(LoginFormDTO loginFormDTO);

    Result adminLogin(LoginFormDTO loginFormDTO);
}
