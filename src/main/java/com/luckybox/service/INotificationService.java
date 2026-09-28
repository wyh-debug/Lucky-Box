package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.NotificationDTO;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Notification;

public interface INotificationService extends IService<Notification> {
    Result sendToAdmin(NotificationDTO notificationDTO);
}
