package com.moa.baselib.utils;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author wangjian
 */
public class StringUtils {

    public static final String REGEX_MOBILE = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    public static final String REGEX_NUM = "^\\d+$";
    public static final String REGEX_GET = "凭证码：([\\d]{3,})你好";
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    public static final String REGEX_DECIMAL = "^[0-9]+(.[0-9]{2})?$";
    public static final String REGEX_LOW_CHAR = "^[a-z]+$";
    public static final String REGEX_UP_LOW_CHAR = "^[A-Za-z]+$";
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 验证小写字母
     *
     * @param input 输入的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLowChar(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_LOW_CHAR);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 验证验证输入字母
     *
     * @param input 输入的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUpAndLowChar(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_UP_LOW_CHAR);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 验证验证输入汉字
     *
     * @param input 输入的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChinese(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_CHINESE);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 验证网址Url
     *
     * @param input 输入的字符串
     * @return 是否正确的url格式
     */
    public static boolean isUrl(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_URL);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 验证输入两位小数
     *
     * @param input 输入的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isDecimal(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_DECIMAL);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 获取特殊字符串中的数字，此正则需要根据需要修改
     *
     * @param input 输入的字符串
     * @return 提取到的字符串
     */
    public static String getExpectTxt(String input) {
        if(TextUtils.isEmpty(input)) return "";
        Pattern pattern = Pattern.compile(REGEX_GET);
        Matcher match = pattern.matcher(input);
        if (match.find()) {
            return match.group(1);
        }
        return "";
    }

    /**
     * 数字验证
     *
     * @param input 输入的字符串
     * @return 是否全是数字
     */
    public static boolean isNumber(String input) {
        if(TextUtils.isEmpty(input)) return false;
        Pattern pattern = Pattern.compile(REGEX_NUM);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 手机号验证
     *
     * @param input 输入的字符串
     * @return 是否是正确的手机号
     */
    public static boolean isPhone(String input) {
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * 金额验证
     *
     * @param input 输入的字符串
     * @return 是否是正确的金额数字
     */
    public static boolean isPrice(String input) {
        // 判断小数点后2位的数字的正则表达式
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d{1,2})?)");
        Matcher match = pattern.matcher(input);
        return match.matches();
    }

    /**
     * list拼接成字符串
     *
     * @param list      数据
     * @param separator 分隔符
     * @return 字符串
     */
    public static String listToString(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }

}
