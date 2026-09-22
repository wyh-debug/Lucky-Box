package com.luckybox.controller;

import com.luckybox.pojo.dto.LoginFormDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.service.IAuthService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private IAuthService authService;
    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone) {
        return authService.sendCode(phone);
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO) {
        return authService.login(loginFormDTO);
    }

    @PostMapping("/admin/login")
    public Result adminLogin(@RequestBody LoginFormDTO loginFormDTO) {
        return authService.adminLogin(loginFormDTO);
    }

}
