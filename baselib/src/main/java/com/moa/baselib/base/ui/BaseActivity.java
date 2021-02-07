package com.moa.baselib.base.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.leaf.library.StatusBarUtil;
import com.moa.baselib.BaseApp;
import com.moa.baselib.R;
import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.LogUtils;
import com.moa.baselib.utils.ProgressDialog;
import com.moa.baselib.utils.SystemBarTintManager;
import com.moa.baselib.utils.ToastUtils;

/**
 * activity 基类
 * <p>
 * Created by：wangjian on 2017/12/22 11:32
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_DATA = "EXTRA_DATA";

    protected SystemBarTintManager mTintManager;
    protected Toolbar mToolbar;
    protected View customView;
    protected TextView tvTitle;
    protected TextView tvRight;
    protected ImageView ivBack;
    protected View vDivider;

    @Override
    protected void attachBaseContext(Context newBase) {
        // 更新多语言
        Context context = AppUtils.updateConfiguration(newBase);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getStatusBarColor());
        StatusBarUtil.setDarkMode(this);
        super.onCreate(savedInstanceState);
        BaseApp.addActivity(this);

        // 1.init intent
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            getSavedData(bundle);
        }

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }

        // init header
        initHeader();
        // init view
        initView();
        // init data
        initData();

        // setWindowStatusBarColor(this, R.color.tt_colorAccent);
    }

    /**
     * 设置导航栏背景颜色
     *
     * @param colorResId color
     */
    public void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止dialog显示造成内存泄露
        hideProgress();
        BaseApp.removeActivity(this);
    }

    /**
     * Get the catch data from bundle
     *
     * @param bundle
     */
    protected void getSavedData(Bundle bundle) {
    }

    /**
     * layout id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * init titlebar
     */
    protected void initHeader() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar == null) {
            LogUtils.d("without toolbar with id toolbar");
            return;
        }

        setSupportActionBar(mToolbar);

        // the default layout
        customView = View.inflate(this, R.layout.tt_toolbar_custom_view, null);
        tvTitle = customView.findViewById(R.id.tv_title);
        ivBack = customView.findViewById(R.id.iv_back);
        vDivider = customView.findViewById(R.id.v_divider);
        tvRight = customView.findViewById(R.id.tv_right);

        ivBack.setOnClickListener(this);

        setToolbar(customView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
                false);
    }

    /**
     * 显示返回按钮
     *
     * @param canback
     */
    public void showBackButton(boolean canback) {
        if (ivBack != null) {
            ivBack.setVisibility(canback ? View.VISIBLE : View.GONE);
            vDivider.setVisibility(canback ? View.VISIBLE : View.GONE);
        }
    }

    public TextView getRightTv(){
        tvRight.setVisibility(View.VISIBLE);
        return tvRight;
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View view) {
        if (ivBack != null && view == ivBack) {
            // 自定义返回按钮事件
            if(onBackImgClick()){
                return;
            }
            onBackPressed();
        }
    }

    /**
     * 自定义返回按钮事件
     *
     * @return true 执行覆盖并执行自定义事件，false 执行系统onBackPressed方法
     */
    protected boolean onBackImgClick(){
        return false;
    }

    public void setOnClickListener(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * init layout view
     */
    protected void initView() {
    }

    /**
     * Deal data
     */
    protected void initData() {
    }

    // state bar
    // public int getStatusBarHeight() {
    //     if (mTintManager != null) {
    //         return mTintManager.getConfig().getStatusBarHeight();
    //     }
    //     return 0;
    // }

    /**
     * statebar color
     *
     * @return
     */
    protected int getStatusBarColor() {
        return AppUtils.getColor(this, R.color.tt_colorPrimaryDark);
    }


    // Toolbar

    protected void setToolbar(View customView, ActionBar.LayoutParams params) {
        setToolbar(customView, params, true);
    }

    protected void setToolbar(View customView, ActionBar.LayoutParams customParams, boolean enableBack) {
        if (getSupportActionBar() == null) {
            throw new RuntimeException("Action bar is not set!");
        }
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setCustomView(customView, customParams);
        if (enableBack) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            Toolbar parent = (Toolbar) customView.getParent();
            parent.setContentInsetsAbsolute(0, 0);
        }
    }

    public void replaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        replaceFragment(fragment, containerId, fragment.getClass().getName(), addToBackStack);
    }

    /**
     * 替换显示fragment
     *
     * @param fragment       要显示的fragment
     * @param containerId    显示fragment的容器
     * @param tag            保存的tag
     * @param addToBackStack 是否添加fragment到返回栈中
     */
    public void replaceFragment(Fragment fragment, int containerId, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 通过hide和show来隐藏和显示fragment
     *
     * @param hideFragment 需要隐藏的fragment
     * @param showFragment 需要显示的fragment
     * @param containerId  fragment容器id
     * @param addToBackStack 是否添加fragment到返回栈中
     */
    public void hideOrShowFragment(Fragment hideFragment, Fragment showFragment, int containerId, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先判断是否被add过
        if (!showFragment.isAdded()) {
            // 隐藏当前的hideFragment，显示showFragment
            if(hideFragment != null){
                transaction.hide(hideFragment);
            }

            transaction .add(containerId, showFragment, showFragment.getClass().getName());
        } else {
            // 隐藏当前的hideFragment，显示showFragment
            transaction.hide(hideFragment).show(showFragment);
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();
    }

    /**
     * 获取当前容器中显示的fragment
     *
     * @param containerId 容器id
     * @return 当前fragment
     */
    public Fragment findFragment(int containerId) {
        return getSupportFragmentManager().findFragmentById(containerId);
    }

    // ========================================
    // =====以下处理加载框显示隐藏
    //=========================================

    /**
     * 加载等待框
     */
    protected ProgressDialog mProgressDialog;

    /**
     * 显示加载框
     */
    protected void showProgress() {
        showProgress(false, getString(R.string.tt_loading));
    }

    /**
     * 显示带取消按钮的加载框
     */
    protected void showCancelableProgress() {
        showProgress(true, getString(R.string.tt_loading));
    }

    /**
     * 显示加载框
     *
     * @param cancelEnable 是否显示取消按钮
     * @param progressText 加载中，显示的文本
     */
    protected void showProgress(boolean cancelEnable, String progressText) {
        mProgressDialog = ProgressDialog.getInstance(this);
        if (cancelEnable) {
            mProgressDialog.setRequestCancelEnable(true, v -> onProgressCancelRequest());
        }

        if (!TextUtils.isEmpty(progressText)) {
            mProgressDialog.setProgressText(progressText);
        }

        mProgressDialog.showProgress();
    }

    /**
     * 隐藏加载框
     */
    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismissProgress();
        }
    }

    /**
     * 在设置可取消加载框时，点击取消按钮回调次方法
     */
    protected void onProgressCancelRequest() {
    }

    // ========================================
    // =====以下处理Toast显示隐藏
    //=========================================

    protected void showToast(String text) {
        ToastUtils.showToast(this, text);
    }

    protected void showToast(int resId) {
        ToastUtils.showToast(this, resId);
    }
}
