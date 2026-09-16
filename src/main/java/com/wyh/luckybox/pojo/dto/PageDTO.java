package com.wyh.luckybox.pojo.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class PageDTO{
    // 总页数
    private Long pages;
    //总条数
    private Long total;
    private Long current;    // 当前页
    private Long size;       // 每页条数
    private List<?> data;
//转换 可以后加我没写
    public static <PO, VO> PageDTO build(Page<PO> page, Class<VO> clazz) {
        PageDTO result = new PageDTO();
        result.setPages(page.getPages());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        List<PO> poList = page.getRecords();
        if(poList == null || poList.isEmpty()) {
            result.setData(Collections.emptyList());
            return result;
        }
        List<VO> voList = poList.stream()
                .map(po -> {
                    return BeanUtil.copyProperties(po, clazz);
                })
                .collect(Collectors.toList());
        result.setData(voList);
        return result;
    }
}
