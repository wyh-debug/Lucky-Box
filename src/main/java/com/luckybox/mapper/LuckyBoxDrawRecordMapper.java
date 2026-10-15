package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luckybox.pojo.entity.LuckyBoxDrawRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LuckyBoxDrawRecordMapper extends BaseMapper<LuckyBoxDrawRecord> {
    boolean insertBatch(@Param("list") List<LuckyBoxDrawRecord> luckyBoxDrawRecordList);
}
