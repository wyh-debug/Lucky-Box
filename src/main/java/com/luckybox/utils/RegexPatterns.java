package com.luckybox.utils;

public abstract class RegexPatterns {
    public static final String PHONE_REGEX = "^1(3[0-9]|4[5-9]|5[0-35-9]|6[2-7]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";
    public static final String CODE_REGEX = "^[a-zA-Z\\d]{6}$";
    public static final String SQL_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$";

}
