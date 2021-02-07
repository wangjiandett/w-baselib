package com.moa.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.moa.baselib.BaseApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SharedPreferences 工具类，拓展支持保存对象
 *
 * @author wangjian
 * Created on 2020/8/3 9:59
 */
public class SharePreUtils {

    private static final String SP_NAME = "config";
    private static SharedPreferences sp;

    private static void getSharedPreferences() {
        if (sp == null) {
            sp = BaseApp.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void saveBoolean(String key, boolean value) {
        getSharedPreferences();
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        getSharedPreferences();
        return sp.getBoolean(key, defValue);
    }

    public static void saveString(String key, String value) {
        getSharedPreferences();
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        getSharedPreferences();
        return sp.getString(key, defValue);
    }

    public static void saveLong(String key, long value) {
        getSharedPreferences();
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        getSharedPreferences();
        return sp.getLong(key, defValue);
    }

    public static void saveInt(String key, int value) {
        getSharedPreferences();
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        getSharedPreferences();
        return sp.getInt(key, defValue);
    }

    /**
     * 使用SharedPreference保存序列化对象
     * 用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param key    储存对象的key
     * @param object object对象，对象必须实现Serializable序列化，否则会出问题，
     *               out.writeObject 无法写入 Parcelable 序列化的对象
     */
    public static void saveObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            saveString(key, objectVal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(baos, out);
        }
    }

    /**
     * 获取SharedPreference保存的对象
     * 使用Base64解密String，返回Object对象
     *
     * @param key 储存对象的key
     * @param <T> 泛型
     * @return 返回保存的对象
     */
    public static <T> T getObject(String key) {
        getSharedPreferences();

        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            if (!TextUtils.isEmpty(objectVal)) {
                byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(bais);
                    Object value = ois.readObject();
                    if(value == null || TextUtils.equals(value.toString(), "")){
                        return null;
                    }
                    return (T) value;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bais, ois);
                }
            }
        }
        return null;
    }
}
