package com.cai.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author kyle
 * @description: 校验是否包含特殊字符
 * @date 2022/9/8 17:51
 */
public class CheckCharacter {

    /**
     * 校验账号是否包含特殊字符
     * @param LoginId
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean checkLoginId(String LoginId)throws PatternSyntaxException {
            // 只允许字母和数字
            // String regEx ="[^a-zA-Z0-9]";
            // 清除掉所有特殊字符
            String regEx = "[`~!#$%^&*()+=|{}':;',\\[\\] .<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            //Pattern p = Pattern.compile(regEx);
            //Matcher m = p.matcher(str);
            Matcher m = Pattern.compile(regEx).matcher(LoginId);
            //去掉特殊字符
            //return m.replaceAll("").trim();
            return m.find();
    }

    /**
     * 验证Email
     * @param email email地址，格式：zhang@gmail.com，zhang@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }
}
