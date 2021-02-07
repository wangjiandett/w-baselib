package com.moa.rxdemo.mvp.view;

import android.content.Context;
import android.content.Intent;

import com.moa.baselib.base.ui.BaseActivity;
import com.moa.rxdemo.utils.AppConfig;
import com.moa.rxdemo.utils.PrivacyHelper;

/**
 * 闪屏界面
 * <p>
 * Created by：wangjian on 2018/12/20 16:04
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0; // R.layout.tt_activity_splash
    }

    @Override
    protected void initData() {
        super.initData();
        if (AppConfig.getAgreePrivacyPolicy()) {
            PrivacyHelper.jump2MainOrGuide(this);
        } else {
            PrivacyHelper.showPrivacyDialog(this);
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }
}
