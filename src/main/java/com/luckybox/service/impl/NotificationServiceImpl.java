package com.luckybox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luckybox.mapper.NotificationMapper;
import com.luckybox.pojo.entity.Notification;
import com.luckybox.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

}
