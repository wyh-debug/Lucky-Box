package com.wyh.luckybox.constant;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final Long LOGIN_TOKEN_TTL = 36000L;
    public static final String CACHE_PRODUCT_KEY = "cache:product:";
    public static final Long CACHE_PRODUCT_TTL = 36000L;
    public static final Long CACHE_PRODUCT_EXP = 1440L;
    public static final String PRODUCT_LOCK_KEY = "product:lock:";
    public static final String PRODUCT_SKU_KEY = "product:sku:";
    public static final Long PRODUCT_SKU_TTL = 36000L;
    public static final Long PRODUCT_SKU_EXP = 1440L;
}
