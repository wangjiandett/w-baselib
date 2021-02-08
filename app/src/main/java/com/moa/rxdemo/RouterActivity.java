package com.moa.rxdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.moa.baselib.base.ui.BaseActivity;
import com.moa.rxdemo.mvp.view.my.AboutFragment;
import com.moa.rxdemo.mvp.view.my.SingleInputFragment;

/**
 * fragment通用界面中转
 * <p>
 * Created by：wangjian on 2017/12/20 16:25
 */
public class RouterActivity extends BaseActivity {

    public static final int SETTING_PAGE = 0x01; // 设置界面
    public static final int ABOUT_PAGE = 0x02; // 关于界面
    public static final int SUGGESTION_PAGE = 0x21; // 意见反馈界面
    public static final int SCAN_INPUT_PAGE = 0x27; // 扫描输入界面
    public static final int SINGLE_INPUT_PAGE = 0x42; // 单行输入修改

    private int pageType;

    @Override
    protected int getLayoutId() {
        return R.layout.tt_activity_router;
    }

    @Override
    protected void initView() {
        super.initView();
        Intent intent = getIntent();

        if (intent != null) {
            pageType = intent.getIntExtra(EXTRA_DATA, SETTING_PAGE);
            Bundle bundle = intent.getExtras();
            switch (pageType) {
                case ABOUT_PAGE:
                    showFragment(new AboutFragment());
                    break;
                case SINGLE_INPUT_PAGE:
                    showFragment(new SingleInputFragment(), bundle);
                    break;
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected boolean onBackImgClick() {
        return super.onBackImgClick();
    }

    private void showFragment(Fragment fragment, Bundle bundle) {
        if (fragment != null && bundle != null) {
            fragment.setArguments(bundle);
        }
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.fl_container, false);
    }

    public static Intent getIntent(Context context, int pageType) {
        return getIntent(context, pageType, null);
    }

    public static Intent getIntent(Context context, int pageType, Bundle bundle) {
        Intent intent = new Intent(context, RouterActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        intent.putExtra(EXTRA_DATA, pageType);
        return intent;
    }

}
