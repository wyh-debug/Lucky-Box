package com.luckybox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.NotificationMapper;
import com.luckybox.mapper.UserMapper;
import com.luckybox.pojo.dto.NotificationDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Notification;
import com.luckybox.pojo.entity.User;
import com.luckybox.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {
    private final UserMapper userMapper;
    public Result sendToAdmin(NotificationDTO notificationDTO) {
        //找到所有admin
        List<User> admins = userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getRole, "ADMIN"));
        if(admins == null || admins.isEmpty()) {
            return Result.fail("不存在管理员");
        }
        List<Notification> notifications = new ArrayList<>();
        for(User u : admins) {
            Notification notification = BeanUtil.copyProperties(notificationDTO, Notification.class);
            notification.setUserId(u.getId());
            notifications.add(notification);
        }
        boolean suc = this.saveBatch(notifications);
        if(!suc) {
            return Result.fail("添加失败");
        }
        return Result.ok();
    }
}
