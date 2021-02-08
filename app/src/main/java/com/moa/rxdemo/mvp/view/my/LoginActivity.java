package com.moa.rxdemo.mvp.view.my;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.moa.baselib.base.ui.BaseActivity;
import com.moa.rxdemo.R;

/**
 * 登录界面
 * <p>
 * Created by：wangjian on 2017/12/20 16:25
 */
public class LoginActivity extends BaseActivity {

    public static final int LOGIN_PAGE = 0x01;
    public static final int FORGET_PWD_PAGE = 0x02;
    public static final int REGISTER_PAGE = 0x03;

    @Override
    protected int getLayoutId() {
        return R.layout.tt_activity_login_container;
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        setTitle(R.string.login_title);
    }

    @Override
    protected void initView() {
        super.initView();
        dealShowPage();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        dealShowPage();
    }

    private void dealShowPage(){
        Intent intent = getIntent();
        if (intent != null) {
            int pageType = intent.getIntExtra(EXTRA_DATA, LOGIN_PAGE);
            switch (pageType) {
                case LOGIN_PAGE:
                    showLoginFragment(false);
                    break;
            }
        } else {
            showLoginFragment(true);
        }
    }

    @Override
    public void onBackPressed() {
        // 如果当前界面是登录页，再次点击结束
        Fragment fragment = findFragment(R.id.fl_container);
        if (fragment instanceof LoginFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示登陆界面
     */
    public void showLoginFragment(boolean addToBack) {
        showChildFragment(new LoginFragment(), addToBack);
    }

    private void showChildFragment(Fragment fragment, boolean addToBack) {
        replaceFragment(fragment, R.id.fl_container, addToBack);
    }


    public static Intent getLoginIntent(Context context) {
        return getIntent(context, LOGIN_PAGE);
    }

    public static Intent getRegisterIntent(Context context) {
        return getIntent(context, REGISTER_PAGE);
    }

    public static Intent getForgetPwdIntent(Context context) {
        return getIntent(context, FORGET_PWD_PAGE);
    }

    public static Intent getIntent(Context context, int pageType) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_DATA, pageType);
        return intent;
    }

}
