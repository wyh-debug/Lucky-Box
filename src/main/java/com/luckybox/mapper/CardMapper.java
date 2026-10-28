package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luckybox.pojo.dto.CardDTO;
import com.luckybox.pojo.dto.CardSelectDTO;
import com.luckybox.pojo.entity.Card;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

@Mapper
public interface CardMapper extends BaseMapper<Card> {

    /**
     *
     * @param id 卡片类型id
     * @return
     */
    @Select("select exists(select 1 from card where card_type_id = #{id});")
    boolean existsCard(@Param("id") Long id);

    Page<Card> selectCardList(Page page, @Param("dto") CardSelectDTO cardSelectDTO);

    CardDTO getCard(@Param("id") Long id);
}
