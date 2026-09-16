package com.wyh.luckybox.utils;

import cn.hutool.core.util.StrUtil;

import static com.wyh.luckybox.utils.RegexPatterns.*;

public class RegexUtils {
    public static boolean isPhoneInvalid(String phone) {
        return match(phone, PHONE_REGEX);
    }
    public static boolean isCodeInvalid(String code) {
        return match(code, CODE_REGEX);
    }
    public static boolean isSQLKey(String sqlKey) {
        return match(sqlKey, SQL_REGEX);
    }

    private static boolean match(String msg, String regex) {
        if(StrUtil.isBlank(msg)) {
            return false;
        }
        return msg.matches(regex);
    }

}
