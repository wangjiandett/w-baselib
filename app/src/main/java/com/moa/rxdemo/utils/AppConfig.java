package com.moa.rxdemo.utils;

import android.text.TextUtils;

import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.SharePreUtils;
import com.moa.rxdemo.App;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.net.Constant;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/8/4 16:51
 */
public class AppConfig {

    private static final String SP_KEY_LOGIN_INFO = "sp_key_login_info";
    private static final String SP_KEY_USERNAME = "sp_key_username";
    private static final String SP_KEY_NEED_SHOW_GUIDE = "sp_key_need_show_guide";
    private static final String SP_KEY_AGREE_PRIVACY_POLICY = "sp_key_agree_privacy_policy";
    private static final String SP_KEY_BASE_URL = "sp_key_base_url";
    private static final String SP_KEY_IS_DEBUG_MODE_OPEN = "sp_key_is_debug_mode_open";


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    public static void saveLocalBaseUrl(String baseUrl) {
        SharePreUtils.saveString(SP_KEY_BASE_URL, baseUrl);
    }

    /**
     * 获取base url
     */
    public static String getLocalBaseUrl() {
        String url = SharePreUtils.getString(SP_KEY_BASE_URL, Constant.BASE_URL);
        if(TextUtils.isEmpty(url)){
            url = Constant.BASE_URL;
        }
        return url;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 是否开启调试模式
     */
    public static void saveDebugModeOpen(boolean open) {
        SharePreUtils.saveBoolean(SP_KEY_IS_DEBUG_MODE_OPEN, open);
    }

    public static boolean isDebugModeOpen() {
        return SharePreUtils.getBoolean(SP_KEY_IS_DEBUG_MODE_OPEN, false);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 是否显示引导页
     */
    public static void saveNeedShowGuide(boolean needShow) {
        SharePreUtils.saveBoolean(SP_KEY_NEED_SHOW_GUIDE, needShow);
    }

    public static boolean getNeedShowGuide() {
        return SharePreUtils.getBoolean(SP_KEY_NEED_SHOW_GUIDE, true);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 是否同意隐私策略
     *
     * @param agree 是否同意
     */
    public static void saveAgreePrivacyPolicy(boolean agree) {
        SharePreUtils.saveBoolean(SP_KEY_AGREE_PRIVACY_POLICY, agree);
    }

    public static boolean getAgreePrivacyPolicy() {
        return SharePreUtils.getBoolean(SP_KEY_AGREE_PRIVACY_POLICY, false);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 是否显示版本升级提示框，使用versionName和versionCode作为动态键值
     * 防止固定的键值在升级之后没有改变，导致无法升级提醒
     */
    public static void saveShowUpdateDialog(boolean needShow) {
        SharePreUtils.saveBoolean(AppUtils.getVersionNameAndCode(), needShow);
    }

    public static boolean getShowUpdateDialog() {
        return SharePreUtils.getBoolean(AppUtils.getVersionNameAndCode(), true);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 保存登录信息
     *
     * @param response 用户登录信息
     */
    public static void saveLoginInfo(UserInfoResponse response) {
        SharePreUtils.saveObject(SP_KEY_LOGIN_INFO, response);
        App.getAppInstance().getUserInfoLiveData().setValue(response);
    }

    /**
     * 获取保存的登录信息，没有登录时返回空
     *
     * @return 登录信息
     */
    public static UserInfoResponse getLoginInfo() {
        return SharePreUtils.getObject(SP_KEY_LOGIN_INFO);
    }

}
