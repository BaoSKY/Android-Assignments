package com.example.studycourse.Util;

public class RegexUtil {
    /**
     * 正则表达式:验证手机号
     */
    private static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 正则表达式:验证邮箱
     */
    private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static boolean isPhone(String value){
        return value.matches(REGEX_MOBILE);
    }

    public static boolean isEmail(String value){
        return value.matches(REGEX_EMAIL);
    }


}
