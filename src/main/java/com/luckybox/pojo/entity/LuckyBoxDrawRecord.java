package com.luckybox.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("lucky_box_draw_record")
public class LuckyBoxDrawRecord implements Serializable {
    private static final long serialVersionUID = 20L;

    @TableId(type = IdType.AUTO)
    private Long id;                          // 抽奖记录ID

    private Long baseOrderId;                 // 所属订单ID
    private Long luckyBoxId;                  // 盲盒ID
    private Integer drawIndex;                // 第几次抽（1~N）

    // ⭐ 中奖商品（主奖品）
    private Long winningProductSkuId;         // 中奖商品SKU ID

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> winningProductSnapshot;  // 中奖商品快照

    // ⭐ 附赠卡片（可能为空）
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String, Object>> cardsDrawn;        // 附赠卡片快照

    private LocalDateTime createdTime;           // 抽奖时间

    private Long productOrderId;              // 关联发货单ID
    private Integer status = 0;
    private Long addressId;
}
