package com.wyh.luckybox.pojo.entity;

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

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;     // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;                // 创建人ID

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;                 // 更新人ID

    private Long luckyBoxOrderId;        // 盲盒订单ID
    private Long baseOrderId;             // 盲盒ID
    private String luckyBoxSnapshot;     // 盲盒信息快照（JSON）
    private Integer luckyBoxCount;       // 盲盒数量
    private Long winningProductSkuId;      // 抽中的商品SKU ID
    private String winningProductSnapshot; // 中奖商品快照（JSON）
    private Long productOrderId;           // 关联的商品订单ID（发货单）
    private String cardsDrawn;             // 附赠的卡片列表快照（JSON）
}