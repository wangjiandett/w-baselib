package com.moa.rxdemo.mvp.view.my;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.StringUtils;
import com.moa.baselib.view.CountDownTextView;
import com.moa.rxdemo.R;
import com.moa.rxdemo.mvp.bean.RegisterRequest;
import com.moa.rxdemo.weiget.SettingCodeItemView;
import com.moa.rxdemo.weiget.SettingEditItemView;
import com.moa.rxdemo.weiget.SettingPicCodeItemView;

import org.jetbrains.annotations.NotNull;

/**
 * 注册界面
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class RegisterFragment extends BaseFragment  {


    public RegisterFragment() {
    }

    private ImageView mIvLogo;
    private SettingEditItemView mUsernameView;
    private SettingEditItemView mPwdView;
    private SettingPicCodeItemView mAuthCode;
    private ImageView mPicCodeIv;
    private SettingCodeItemView mCodeView;
    private CountDownTextView mCountDownTextView;
    private View mSubmitBtn;
    private CheckBox mAgreementCb;
    private TextView mAgreementDesc;
    private TextView mGoLogin;


    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_register;
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        setTitle(R.string.register_title);
    }

    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);

        mIvLogo = findViewById(R.id.iv_logo);
        mUsernameView = findViewById(R.id.username_view);
        mPwdView = findViewById(R.id.pwd_view);
        mAuthCode = findViewById(R.id.auth_code_view);
        mPicCodeIv = mAuthCode.getIvCodeIcon();

        mCodeView = findViewById(R.id.check_view);
        mCountDownTextView = mCodeView.getCountDownTv();
        mSubmitBtn = findViewById(R.id.btn_submit);
        mAgreementCb = findViewById(R.id.cb_agreement);
        mAgreementDesc = findViewById(R.id.tv_agreement_desc);
        mGoLogin = findViewById(R.id.tv_go_login);

        setOnClickListener(mSubmitBtn, mCountDownTextView, mGoLogin, mAgreementDesc, mPicCodeIv);

        // 底部文字样式
        SpannableString string = new SpannableString(getString(R.string.login_go_login_tip));
        string.setSpan(new ForegroundColorSpan(AppUtils.getColor(getActivity(), R.color.tt_colorAccent)),
                5, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mGoLogin.setText(string);

        // 用户名
        mUsernameView.setInputTypePhone();
        mUsernameView.getInputEt().setHint(R.string.login_input_username);
        mUsernameView.setStartIcon(R.mipmap.cell_phone);

        // 密码
        mPwdView.getInputEt().setHint(R.string.login_input_pwd);
        mPwdView.setInputTypePwd();
        mPwdView.setStartIcon(R.mipmap.account_password);
        mPwdView.getEndIv().setImageResource(R.drawable.pwd_eyes_selector);
        mPwdView.setOnEndIconClickListener(v -> {
            mPwdView.getEndIv().setSelected(!v.isSelected());
            mPwdView.togglePwdVisible();
        });

        // 图片验证码
        mAuthCode.setStartIcon(R.mipmap.verification_code);
        mAuthCode.getInputEt().setHint(R.string.login_input_pic_code);

        // 短信验证码
        mCodeView.getInputEt().setHint(R.string.login_input_check_code);
        mCodeView.setStartIcon(R.mipmap.verification_code);
        // 验证码倒计时样式
        mCountDownTextView.setTipText(getString(R.string.login_get_check_code));
        mCountDownTextView.setShowFormatResId(R.string.get_check_code_desc);
        mCountDownTextView.setTimeNeedFormat(false);
        mCountDownTextView.setClickEnable(false);

        // 监听文本变化，改变注册按钮状态
        mUsernameView.setTextChangeListener(s -> updateRegisterBtnStatus());
        mPwdView.setTextChangeListener(s -> updateRegisterBtnStatus());
        mCodeView.setTextChangeListener(s -> updateRegisterBtnStatus());
        mAuthCode.setTextChangeListener(s -> {
            mCountDownTextView.setClickEnable(!TextUtils.isEmpty(s));
            updateRegisterBtnStatus();
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == mSubmitBtn) {
            checkInput();
        } else if (view == mCountDownTextView) {
            mCountDownTextView.start(60, 1);
        } else if (view == mGoLogin) {
            ((LoginActivity) getActivity()).showLoginFragment(true);
        } else if (view == mAgreementDesc) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCountDownTextView.stop();
    }

    /**
     * 更新底部注册按钮状态
     */
    private void updateRegisterBtnStatus() {
        String username = mUsernameView.getInputEt().getText().toString();
        String checkCode = mCodeView.getInputEt().getText().toString();
        String pwd = mPwdView.getInputEt().getText().toString();
        String authCode = mAuthCode.getInputEt().getText().toString();
        boolean enable = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(checkCode) && !TextUtils.isEmpty(pwd)  && !TextUtils.isEmpty(authCode);
        mSubmitBtn.setEnabled(enable);
    }

    private void checkInput() {
        String username = mUsernameView.getInputEt().getText().toString();
        if (TextUtils.isEmpty(username) || !StringUtils.isPhone(username)) {
            showToast(R.string.login_input_username);
            return;
        }

        String checkCode = mCodeView.getInputEt().getText().toString();
        if (TextUtils.isEmpty(checkCode)) {
            showToast(R.string.login_input_check_code);
            return;
        }

        String pwd = mPwdView.getInputEt().getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showToast(R.string.login_input_pwd);
            return;
        }

        if (!mAgreementCb.isChecked()) {
            showToast(R.string.login_agreement);
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.account = username;
        registerRequest.password = pwd;
        registerRequest.code = checkCode;

        // showProgress();
    }
}
