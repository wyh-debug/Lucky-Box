package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("lucky_box_order")
public class LuckyBoxOrder implements Serializable {
    private static final long serialVersionUID = 21L;
    @TableId(value = "base_order_id", type = IdType.INPUT)
    private Long id;// 盲盒订单ID
    private String luckyBoxSnapshot;     // 盲盒信息快照（JSON）
    private Long luckyBoxId;
    private Integer luckyBoxCount;       // 盲盒数量
    private Long productOrderId;           // 关联的商品订单ID（发货单）
}