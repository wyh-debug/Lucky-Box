package com.wyh.luckybox.interceptor;

import com.wyh.luckybox.pojo.dto.UserDTO;
import com.wyh.luckybox.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = UserHolder.getUser();
        if(Objects.isNull(userDTO)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
