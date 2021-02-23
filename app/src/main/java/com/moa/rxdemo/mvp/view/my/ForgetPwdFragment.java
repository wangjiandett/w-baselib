package com.moa.rxdemo.mvp.view.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.StringUtils;
import com.moa.baselib.view.CountDownTextView;
import com.moa.rxdemo.R;
import com.moa.rxdemo.weiget.SettingCodeItemView;
import com.moa.rxdemo.weiget.SettingEditItemView;
import com.moa.rxdemo.weiget.SettingPicCodeItemView;

import org.jetbrains.annotations.NotNull;

/**
 * 忘记密码
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class ForgetPwdFragment extends BaseFragment {


    public ForgetPwdFragment() {
    }

    private ScrollView mScrollView;
    private LinearLayout mContent;
    private ImageView mIvLogo;
    private SettingEditItemView mUsernameView;
    private SettingEditItemView mPwdView;
    private SettingCodeItemView mCodeView;
    private SettingPicCodeItemView mAuthCode;
    private ImageView mPicCodeIv;
    private CountDownTextView mCountDownTextView;
    private View mSubmitBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_forget_pwd;
    }

    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);

        setTitle(R.string.login_forget_pwd);

        mScrollView = findViewById(R.id.scrollView);
        mContent = findViewById(R.id.content);
        mUsernameView = findViewById(R.id.username_view);
        mPwdView = findViewById(R.id.pwd_view);
        mCodeView = findViewById(R.id.check_view);
        mCountDownTextView = mCodeView.getCountDownTv();
        mAuthCode = findViewById(R.id.auth_code_view);
        mPicCodeIv = mAuthCode.getIvCodeIcon();

        mSubmitBtn = findViewById(R.id.btn_submit);
        mSubmitBtn.setEnabled(false);

        TextView mSubText = findViewById(R.id.tv_sub_text);
        mSubText.setText(R.string.finish);

        setOnClickListener(mSubmitBtn, mCountDownTextView, mPicCodeIv);

        mUsernameView.setInputTypePhone();
        mUsernameView.getInputEt().setHint(R.string.login_input_username);
        mUsernameView.setStartIcon(R.mipmap.cell_phone);

        mPwdView.getInputEt().setHint(R.string.login_input_pwd_hint);
        mPwdView.setInputTypePwd();
        mPwdView.setStartIcon(R.mipmap.account_password);
        mPwdView.setOnEndIconClickListener(v -> {
            mPwdView.togglePwdVisible();
        });

        mCodeView.getInputEt().setHint(R.string.login_input_check_code);
        mCodeView.setStartIcon(R.mipmap.verification_code);
        // 验证码倒计时样式
        mCountDownTextView.setTipText(getString(R.string.login_get_check_code));
        mCountDownTextView.setShowFormatResId(R.string.get_check_code_desc);
        mCountDownTextView.setTimeNeedFormat(false);
        mCountDownTextView.setClickEnable(false);

        // 图片验证码
        mAuthCode.setStartIcon(R.mipmap.verification_code);
        mAuthCode.getInputEt().setHint(R.string.login_input_pic_code);

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
        }
    }

    /**
     * 更新底部完成按钮状态
     */
    private void updateRegisterBtnStatus() {
        String username = mUsernameView.getInputEt().getText().toString();
        String checkCode = mCodeView.getInputEt().getText().toString();
        String pwd = mPwdView.getInputEt().getText().toString();
        String authCode = mAuthCode.getInputEt().getText().toString();
        boolean enable = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(checkCode) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(authCode);
        mSubmitBtn.setEnabled(enable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCountDownTextView.stop();
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
    }
}
