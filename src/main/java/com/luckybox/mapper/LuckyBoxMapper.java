package com.luckybox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luckybox.pojo.dto.LuckyBoxSelectDTO;
import com.luckybox.pojo.entity.LuckyBox;
import com.luckybox.pojo.vo.LuckyBoxVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface LuckyBoxMapper extends BaseMapper<LuckyBox> {
    @Select("SELECT EXISTS(SELECT 1 FROM lucky_box WHERE category_id = #{categoryId})")
    boolean existsByCategoryId(@Param("categoryId") Long categoryId);

    Page<LuckyBoxVO> getBoxList(Page page, @Param("dto") LuckyBoxSelectDTO luckyBoxSelectDTO);

    void updateBatchByIds(@Param("boxIds") Set<Long> boxIds);

    @Select("select lb.*, lbc.name as categoryName from lucky_box as lb left join lucky_box_category as lbc on lb.category_id = lbc.id where lb.id = #{boxId}")
    LuckyBox selectBox(@Param("boxId") Long boxId);
}
