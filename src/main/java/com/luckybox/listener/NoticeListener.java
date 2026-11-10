package com.luckybox.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luckybox.constant.MqConstants;
import com.luckybox.constant.SystemConstants;
import com.luckybox.mapper.NotificationMapper;
import com.luckybox.mapper.UserMapper;
import com.luckybox.pojo.dto.NoticeMessage;

import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.Notification;
import com.luckybox.pojo.entity.User;

import com.luckybox.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeListener {
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConstants.NOTICE_INSERT_QUEUE, durable = "true"),
            exchange = @Exchange(value = MqConstants.NOTICE_TOPIC_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.NOTICE_INSERT_KEY
    ))
    @Transactional
    public void noticeAdd(NoticeMessage msg) {

        List<User> admins = userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getRole, "ADMIN"));
        if(admins == null || admins.isEmpty()) {
            return;
        }
        String ids = msg.getId() == null ? msg.getIds().toString() : msg.getId().toString();
        List<Notification> notifications = new ArrayList<>();
        for(User u : admins) {
            Notification notification = new Notification();
            notification.setTitle("盲盒规则为空通知");
            notification.setContent("盲盒：" + ids + "关联规则为空");
            notification.setType(SystemConstants.NOTIFICATION_TYPE_BOX);
            notification.setUserId(u.getId());
            notifications.add(notification);
        }
        notificationMapper.saveBatch(notifications);
    }
}
