package com.moa.rxdemo;

import android.arch.lifecycle.MutableLiveData;

import com.moa.baselib.BaseApp;
import com.moa.baselib.utils.AppUtils;
import com.moa.rxdemo.db.AppDatabase;
import com.moa.rxdemo.db.DataRepository;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.mvp.view.service.WebSocketService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 项目地址 <a href="https://github.com/wangjiandett/RxDemo">https://github.com/wangjiandett/RxDemo</a>
 * 程序入口
 * <p>
 * Created by：wangjian on 2017/12/20 15:45
 */
public class App extends BaseApp {

    private static final String TAG = "App";
    protected static App mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        if (AppUtils.isMainProcess(this)) {
            // 启动web socket
            WebSocketService.startService(this);
        }
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 以下2个方法获得对room数据库管理是实例
    public static DataRepository getDataRepository() {
        return DataRepository.getInstance(getAppDatabase());
    }

    public static AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(getAppContext());
    }

    public static App getAppInstance() {
        return mAppInstance;
    }

    /**
     * 用于监听登录信息变化，动态更新界面显示
     */
    private MutableLiveData<UserInfoResponse> userInfoLiveData = new MutableLiveData<>();

    public MutableLiveData<UserInfoResponse> getUserInfoLiveData() {
        return userInfoLiveData;
    }
}
