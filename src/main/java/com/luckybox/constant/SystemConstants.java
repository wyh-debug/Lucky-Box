package com.luckybox.constant;

public class SystemConstants {

    public static final Integer ENABLE = 0;
    public static final Integer DISABLE = 1;
    public static final Integer PRICE_UP = 0;
    public static final Integer PRICE_DOWN = 1;
    public static final Integer SALE_UP = 2;
    public static final String USER_NICK_NAME_PREFIX = "user_";
    //订单分类
    public static final Integer ORDER_TYPE_PRODUCT = 0;
    public static final Integer ORDER_TYPE_BOX = 1;

    //'订单状态：PENDING-待支付, PAID-已支付, SHIPPED-已发货, COMPLETED-已完成, CANCELLED-已取消, REFUNDED-已退款',
    //订单状态
    public static final Integer ORDER_STATUS_PENDING  = 0;
    public static final Integer ORDER_STATUS_PAID  = 1;
    public static final Integer ORDER_STATUS_SHIPPED  = 2;
    public static final Integer ORDER_STATUS_COMPLETED = 3;
    public static final Integer ORDER_STATUS_CANCELLED = 4;
    public static final Integer ORDER_STATUS_REFUNDED  = 5;

    //地址 0 不置顶 1 置顶
    public static final Boolean ADDRESS_NOT_TOP  = false;
    public static final Boolean ADDRESS_IS_TOP  = true;

    // 盲盒类型：0-商品盲盒, 1-抽卡盲盒
    public static final Integer BOX_TYPE_PRODUCT  = 0;
    public static final Integer BOX_TYPE_CARD  = 1;




}
