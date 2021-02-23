package com.moa.rxdemo.mvp.view.my;

import android.view.View;
import android.widget.TextView;

import com.moa.baselib.base.net.mvp.SimpleValueCallback;
import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.AppUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.Router;
import com.moa.rxdemo.mvp.bean.CheckUpdateResponse;
import com.moa.rxdemo.mvp.model.CheckUpdateModel;
import com.moa.rxdemo.utils.AppConfig;
import com.moa.rxdemo.utils.UpdateHelper;
import com.moa.rxdemo.weiget.SettingItemView;

import org.jetbrains.annotations.NotNull;

/**
 * 关于我们
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class AboutFragment extends BaseFragment {

    private static final int ENTER_TIMES = 5;
    private int mClickTimes = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_about_us;
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        setTitle(R.string.about_us);
    }

    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);

        TextView mVersionName = findViewById(R.id.tv_version_name);
        mVersionName.setText(String.format("%s %s", getString(R.string.app_name), AppUtils.getVersionName()));

        SettingItemView updateView = findViewById(R.id.update_view);
        updateView.getTitleTv().setText(R.string.check_update);
        // updateView.getSubTitleTv().setText(AppUtils.getVersionName());

        updateView.setOnClickListener(v -> {
            checkUpdate();
        });

        SettingItemView userAgreementView = findViewById(R.id.user_agreement_view);
        userAgreementView.getTitleTv().setText(R.string.user_user_agreement);
        userAgreementView.setOnClickListener(v -> {
            Router.goUserAgreement(v.getContext());
        });

        SettingItemView privacyPolicyView = findViewById(R.id.privacy_policy_view);
        privacyPolicyView.getTitleTv().setText(R.string.user_privacy_policy);
        privacyPolicyView.setOnClickListener(v -> {
            Router.goPrivacyPolicy(v.getContext());
        });

        findViewById(R.id.iv_head_icon).setOnClickListener(v -> {
            boolean isOpen = AppConfig.isDebugModeOpen();
            if (isOpen) {
                Router.goDebug(getActivity());
            } else {
                // 进入debug
                mClickTimes++;
                // 点击2次后才提示，防止用户误触
                if (mClickTimes > 2 && mClickTimes < ENTER_TIMES) {
                    showToast(getString(R.string.click_enter_develop_model, (ENTER_TIMES - mClickTimes)));
                }

                if (mClickTimes >= ENTER_TIMES) {
                    AppConfig.saveDebugModeOpen(true);
                    Router.goDebug(getActivity());
                }
            }
        });
    }

    private void checkUpdate() {
        showProgress();

        new CheckUpdateModel().checkUpdate(AppUtils.getVersionName(), new SimpleValueCallback<CheckUpdateResponse>() {
            @Override
            public void onSuccess(CheckUpdateResponse response) {
                hideProgress();
                UpdateHelper.dealUpdateValue(getActivity(), response, false);
            }

            @Override
            public void onFail(String msg) {
                showToast(msg);
                hideProgress();
                UpdateHelper.dealUpdateValue(getActivity(), null, false);
            }
        });
    }

    @Override
    protected void onProgressCancelRequest() {
        showToast(R.string.logout_title);
    }
}
