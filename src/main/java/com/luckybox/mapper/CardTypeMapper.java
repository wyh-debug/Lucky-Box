package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luckybox.pojo.dto.CardTypeSelectDTO;
import com.luckybox.pojo.entity.CardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CardTypeMapper extends BaseMapper<CardType> {
    Page<CardType> selectCardTypes(Page page, @Param("dto") CardTypeSelectDTO cardTypeSelectDTO);
}
