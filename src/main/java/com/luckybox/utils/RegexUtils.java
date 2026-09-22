package com.luckybox.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    public static boolean isPhoneInvalid(String phone) {
        return match(phone, RegexPatterns.PHONE_REGEX);
    }
    public static boolean isCodeInvalid(String code) {
        return match(code, RegexPatterns.CODE_REGEX);
    }
    public static boolean isSQLKey(String sqlKey) {
        return match(sqlKey, RegexPatterns.SQL_REGEX);
    }

    private static boolean match(String msg, String regex) {
        if(StrUtil.isBlank(msg)) {
            return false;
        }
        return msg.matches(regex);
    }

}
