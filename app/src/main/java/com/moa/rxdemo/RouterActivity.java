package com.moa.rxdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.moa.baselib.base.ui.BaseActivity;

/**
 * fragment通用界面中转
 * <p>
 * Created by：wangjian on 2017/12/20 16:25
 */
public class RouterActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.tt_activity_router;
    }

    @Override
    protected void initView() {
        super.initView();
        Intent intent = getIntent();
        if (intent != null) {
            try {
                Class<?> mFragmentClass = (Class<?>) intent.getSerializableExtra(EXTRA_DATA);
                Bundle bundle = intent.getExtras();
                showFragment((Fragment) mFragmentClass.newInstance(), bundle);
            } catch (Exception e) {
                e.printStackTrace();
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

    public static Intent getIntent(Context context, Class<?> clazz) {
        return getIntent(context, clazz, null);
    }

    public static Intent getIntent(Context context, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(context, RouterActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        intent.putExtra(EXTRA_DATA, clazz);
        return intent;
    }

}
