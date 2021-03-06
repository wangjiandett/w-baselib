package com.moa.rxdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.moa.baselib.base.ui.H5Activity;
import com.moa.rxdemo.mvp.view.MainActivity;
import com.moa.rxdemo.mvp.view.ScanActivity;
import com.moa.rxdemo.mvp.view.SplashActivity;
import com.moa.rxdemo.mvp.view.guide.GuideActivity;
import com.moa.rxdemo.mvp.view.my.AboutFragment;
import com.moa.rxdemo.mvp.view.my.DebugFragment;
import com.moa.rxdemo.mvp.view.my.LoginActivity;
import com.moa.rxdemo.mvp.view.my.LoginFragment;
import com.moa.rxdemo.mvp.view.my.SingleInputFragment;

/**
 * 路由到各个界面
 *
 * @author wangjian
 * Created on 2020/8/2 15:40
 */
public class Router {

    /**
     * 跳转到启动页界面
     *
     * @param context context
     */
    public static void goSplash(Context context) {
        goActivity(context, SplashActivity.getIntent(context));
    }

    /**
     * 跳转到引导界面
     *
     * @param context context
     */
    public static void goGuide(Context context) {
        goActivity(context, GuideActivity.getIntent(context));
    }

    /**
     * 跳转到登录界面
     *
     * @param context context
     */
    public static void goLogin(Context context) {
        goActivity(context, LoginActivity.getLoginIntent(context));
    }

    /**
     * 跳转到登录界面
     *
     * @param context context
     */
    public static void goRegister(Context context) {
        goActivity(context, LoginActivity.getRegisterIntent(context));
    }

    /**
     * 跳转到忘记密码界面
     *
     * @param context context
     */
    public static void goForgetPwd(Context context) {
        goActivity(context, LoginActivity.getForgetPwdIntent(context));
    }

    /**
     * 跳转到主界面
     *
     * @param context context
     */
    public static void goMain(Context context) {
        goActivity(context, MainActivity.getIntent(context));
    }

    /**
     * 跳转到设置界面
     *
     * @param context context
     */
    public static void goSetting(Context context) {
        goActivity(context, RouterActivity.getIntent(context, LoginFragment.class));
    }

    /**
     * 跳转到关于界面
     *
     * @param context context
     */
    public static void goAbout(Context context) {
        goActivity(context, RouterActivity.getIntent(context, AboutFragment.class));
    }

    /**
     * 跳转到调试界面
     *
     * @param context context
     */
    public static void goDebug(Context context) {
        goActivity(context, RouterActivity.getIntent(context, DebugFragment.class));
    }

    /**
     * 跳转用户协议
     */
    public static void goUserAgreement(Context context) {
        // String text = FileUtil.readStringAssets(context,"user_agreement.html");
        String text ="https://ahhlhx.com/wx/user_agreement.html";
        H5Activity.go(context, new H5Activity.H5Request(text));
    }

    /**
     * 跳转隐私策略
     */
    public static void goPrivacyPolicy(Context context) {
        // String text = FileUtil.readStringAssets(context,"privacy_policy.html");
        String text ="https://ahhlhx.com/wx/privacy_policy.html";
        H5Activity.go(context, new H5Activity.H5Request(text));
    }

    /**
     * 跳转到扫描输入或手动输入界面
     */
    public static void goScanInput(Fragment fragment, String oldText, int reqCode) {
        Bundle bundle = new Bundle();
        bundle.putString("oldText", oldText);
        goActivity(fragment, RouterActivity.getIntent(fragment.getActivity(), SingleInputFragment.class, bundle), reqCode);
    }

    /**
     * 跳转到扫一扫界面
     *
     * @param fragment 跳转入口界面
     * @param activity 跳转入口界面
     * @param reqCode 请求码
     */
    public static void goScanActivity(Fragment fragment, Activity activity, int reqCode) {
        if(fragment != null){
            goActivity(fragment, ScanActivity.Companion.getIntent(fragment.getContext()), reqCode);
        }else{
            goActivity(activity, ScanActivity.Companion.getIntent(activity), reqCode);
        }
    }















    //
    // ==========================公共跳转方法============================================================
    //


    /**
     * 普通activity跳转
     */
    public static void goActivity(Context context, Intent intent) {
        if (context != null && intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 带请求码的activity跳转
     *
     * @param reqCode 请求码
     */
    public static void goActivity(Activity activity, Intent intent, int reqCode) {
        if (activity != null && intent != null) {
            activity.startActivityForResult(intent, reqCode);
        }
    }

    /**
     * 带请求码的fragment跳转
     *
     * @param reqCode 请求码
     */
    public static void goActivity(Fragment fragment, Intent intent, int reqCode) {
        if (fragment != null && intent != null) {
            fragment.startActivityForResult(intent, reqCode);
        }
    }

}
