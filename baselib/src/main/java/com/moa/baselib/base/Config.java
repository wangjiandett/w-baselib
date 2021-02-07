package com.moa.baselib.base;

import com.moa.baselib.utils.SharePreUtils;

import java.util.Locale;

/**
 * 通用配置文件
 * <p>
 * Created by：wangjian on 2018/12/17 15:50
 */
public class Config {

    private static final String SP_KEY_MULTI_LANGUAGE = "sp_key_multi_language";

    private static volatile Config defaultInstance;
    // 保存系统语言
    private Locale mSystemLocale = Locale.getDefault();
    // 当前语言
    private Locale mCurrentLocale = null;

    private Config() {
    }

    public static Config getDefault() {
        if (defaultInstance == null) {
            synchronized (Config.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Config();
                }
            }
        }
        return defaultInstance;
    }



    //
    // ====================通用配置=========================
    //


    /**
     * 获得当前系统语音
     */
    public Locale getCurrentLocale() {
        if (mCurrentLocale == null) {
            mCurrentLocale = getLocaleByTag(getMultiLanguage(Constants.LAN_AUTO));
        }
        return mCurrentLocale;
    }

    /**
     * 设置语言类型
     *
     * @param language see <br/>
     *                 {@link Constants#LAN_AUTO}<br/>
     *                 {@link Constants#LAN_ENGLISH}<br/>
     *                 {@link Constants#LAN_SIMPLE_CHINESE}<br/>
     */
    public void setCurrentLocale(String language) {
        saveMultiLanguage(language);
        this.mCurrentLocale = getLocaleByTag(language);
    }

    /**
     * 获取当前多语言tag
     * @return <br/>
     *
     *  {@link Constants#LAN_AUTO}<br/>
     *  {@link Constants#LAN_ENGLISH}<br/>
     *  {@link Constants#LAN_SIMPLE_CHINESE}<br/>
     *
     */
    public String getCurrentLocaleTag() {
        return getMultiLanguage(Constants.LAN_AUTO);
    }

    private Locale getLocaleByTag(String localeTag) {
        Locale locale = null;
        // 简体中文
        if (Constants.LAN_SIMPLE_CHINESE.equalsIgnoreCase(localeTag)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        // 英文
        else if (Constants.LAN_ENGLISH.equalsIgnoreCase(localeTag)) {
            locale = Locale.ENGLISH;
        } else {
            // 默认语言
            locale = mSystemLocale;
        }

        return locale;
    }

    /**
     * 保存多语言
     *
     * @param value   see <br/>
     *                {@link Constants#LAN_AUTO}<br/>
     *                {@link Constants#LAN_ENGLISH}<br/>
     *                {@link Constants#LAN_SIMPLE_CHINESE}<br/>
     */
    public static void saveMultiLanguage(String value) {
        SharePreUtils.saveString(SP_KEY_MULTI_LANGUAGE, value);
    }

    /**
     * 获得多语言
     *
     * @param defValue 默认语言
     *
     * @return <br/>
     * {@link Constants#LAN_AUTO}<br/>
     * {@link Constants#LAN_ENGLISH}<br/>
     * {@link Constants#LAN_SIMPLE_CHINESE}<br/>
     */
    public static String getMultiLanguage(String defValue) {
        return SharePreUtils.getString(SP_KEY_MULTI_LANGUAGE, defValue);
    }
}
