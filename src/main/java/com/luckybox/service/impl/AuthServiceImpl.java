package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.RedisConstants;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.AuthMapper;
import com.luckybox.pojo.entity.User;
import com.luckybox.service.IAuthService;
import com.luckybox.utils.RegexUtils;
import com.luckybox.pojo.dto.LoginFormDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthServiceImpl extends ServiceImpl<AuthMapper, User> implements IAuthService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Result sendCode(String phone) {
        if(!RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号格式不正确");
        }
        //发送验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("验证码为：{}", code);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();
        if(!RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号格式不正确");
        }
        if(!RegexUtils.isCodeInvalid(loginFormDTO.getCode())) {
            return Result.fail("验证码不正确");
        }
        String code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        if(!loginFormDTO.getCode().equals(code)) {
            return Result.fail("验证码错误");
        }

        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if(Objects.isNull(user)) {
            user = createUser(phone);
        }
        //生成token
        String token = saveUserToRedis(user);
        return Result.ok(token);
    }

    @Override
    public Result adminLogin(LoginFormDTO adminLoginFormDTO) {
        //账号密码登录
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getRole, "ADMIN").eq(User::getNickname, adminLoginFormDTO.getNickname()));
        if(Objects.isNull(user)) {
            return Result.fail("没有该用户");
        }
        if(Objects.isNull(adminLoginFormDTO.getPassword()) || !bCryptPasswordEncoder.matches(adminLoginFormDTO.getPassword(), user.getPassword())) {
            return Result.fail("用户密码不正确");
        }
        //生成token
        String token = saveUserToRedis(user);
        return Result.ok(token);
    }

    private User createUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickname(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        this.save(user);
        return user;
    }
    private String saveUserToRedis(User user) {
        String token = UUID.randomUUID().toString(true);
        String key = RedisConstants.LOGIN_TOKEN_KEY + token;

        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((k, v) ->  v == null ? null : v.toString()));
        stringRedisTemplate.opsForHash().putAll(key, userMap);
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        return token;
    }
}
