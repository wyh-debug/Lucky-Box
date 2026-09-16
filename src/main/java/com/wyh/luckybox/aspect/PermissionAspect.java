package com.wyh.luckybox.aspect;

import com.wyh.luckybox.annotation.AdminRequired;
import com.wyh.luckybox.pojo.dto.Result;
import com.wyh.luckybox.pojo.dto.UserDTO;
import com.wyh.luckybox.utils.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {
    @Around("@annotation(adminRequired)")
    public Object check(ProceedingJoinPoint pjp, AdminRequired adminRequired) throws Throwable {
        UserDTO userDTO = UserHolder.getUser();
        if(userDTO == null || !"ADMIN".equals(userDTO.getRole())) {
            return Result.fail("权限不足");
        }
        return pjp.proceed();
    }

}
