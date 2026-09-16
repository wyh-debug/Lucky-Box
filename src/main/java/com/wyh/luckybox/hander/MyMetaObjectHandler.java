package com.wyh.luckybox.hander;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wyh.luckybox.pojo.dto.UserDTO;
import com.wyh.luckybox.utils.UserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        UserDTO userDTO = UserHolder.getUser();
        if(Objects.isNull(userDTO)) {
            return;
        }
        Long userId = UserHolder.getUser().getId();
        this.strictInsertFill(metaObject, "creatorId", Long.class, userId);
        this.strictInsertFill(metaObject, "updateId", Long.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        UserDTO userDTO = UserHolder.getUser();
        if(Objects.isNull(userDTO)) {
            return;
        }
        Long userId = UserHolder.getUser().getId();
        this.strictInsertFill(metaObject, "updateId", Long.class, userId);
    }
}
