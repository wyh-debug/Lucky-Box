package com.luckybox.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luckybox.pojo.dto.Result;
import com.luckybox.pojo.entity.LuckyBoxDrawRecord;

public interface ILuckyBoxDrawRecordService extends IService<LuckyBoxDrawRecord> {
    Result create(Long id);
}
