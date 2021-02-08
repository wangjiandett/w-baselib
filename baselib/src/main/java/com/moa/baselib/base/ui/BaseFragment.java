package com.moa.baselib.base.ui;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moa.baselib.R;
import com.moa.baselib.utils.ProgressDialog;
import com.moa.baselib.utils.ToastUtils;


/**
 * BaseFragment基类
 * <p>
 * Created by：wangjian on 2017/12/22 13:55
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private String title;
    private int titleRes;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 1.加载页面布局
        mView = inflater.inflate(getLayoutId(), null);

        // 2.init header
        initHeader();
        // 3.init view
        initView(mView);
        // 4.init data
        initData();

        return mView;
    }

    /**
     * layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    /**
     * init titlebar
     */
    protected void initHeader() {
    }

    /**
     * init layout view
     */
    protected void initView(@NonNull View view) {
    }

    /**
     * Deal data
     */
    protected void initData() {
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(@IdRes int id) {
        return (T) mView.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T $(View parent, @IdRes int id) {
        return (T) parent.findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    public void setOnClickListener(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setOnClickListener(this);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;

        if (getActivity() != null) {
            getActivity().setTitle(title);
        }
    }

    public void setTitle(int titleRes) {
        this.titleRes = titleRes;

        if (getActivity() != null) {
            getActivity().setTitle(titleRes);
        }
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
        mProgressDialog = ProgressDialog.getInstance(getActivity());
        if (cancelEnable) {
            mProgressDialog.setRequestCancelEnable(true, v -> {
                mProgressDialog.dismissProgress();
                onProgressCancelRequest();
            });
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
    }

    /**
     * 在设置可取消加载框时，点击取消按钮回调次方法
     */
    protected void onProgressCancelRequest() {
    }

    public void replaceFragment(Fragment fragment, int containerId, boolean addToBackStack) {
        replaceFragment(fragment, containerId, fragment.getClass().getName(), addToBackStack);
    }

    public void replaceFragment(Fragment fragment, int containerId, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    // ========================================
    // =====以下处理Toast显示隐藏
    //=========================================

    protected void showToast(String text) {
        ToastUtils.showToast(getActivity(), text);
    }

    protected void showToast(int resId) {
        ToastUtils.showToast(getActivity(), resId);
    }
}
