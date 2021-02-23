package com.moa.rxdemo.mvp.view.my;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.StringUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.Router;
import com.moa.rxdemo.weiget.SettingEditItemView;

import org.jetbrains.annotations.NotNull;

/**
 * 登录界面
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class LoginFragment extends BaseFragment {


    public LoginFragment() {
    }

    private ImageView mIvLogo;
    private SettingEditItemView mUsernameView;
    private SettingEditItemView mPwdView;
    private View mBtnLogin;
    private TextView mTvForgetPwd;
    private TextView mTvRegisterNow;


    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_login;
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        setTitle(R.string.login_title);
    }

    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);

        mIvLogo = findViewById(R.id.iv_logo);
        mUsernameView = findViewById(R.id.username_view);
        mPwdView = findViewById(R.id.pwd_view);
        mBtnLogin = findViewById(R.id.btn_submit);
        mTvForgetPwd = findViewById(R.id.tv_forget_pwd);
        mTvRegisterNow = findViewById(R.id.tv_register_now);

        setOnClickListener(mBtnLogin, mTvForgetPwd, mTvRegisterNow);

        mUsernameView.getInputEt().setHint(R.string.login_input_username);
        mUsernameView.getInputEt().setInputType(InputType.TYPE_CLASS_PHONE);
        mUsernameView.setStartIcon(R.mipmap.account_number);
        mUsernameView.setInputTypePhone();

//        mUsernameView.getInputEt().setText("15255823032");
//        mPwdView.getInputEt().setText("123456");

        mPwdView.getInputEt().setHint(R.string.login_input_pwd);
        mPwdView.setInputTypePwd();
        mPwdView.setStartIcon(R.mipmap.account_password);

        mUsernameView.setTextChangeListener(s -> updateLoginBtnStatus());

        mPwdView.setTextChangeListener(s -> updateLoginBtnStatus());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mUsernameView.getInputEt().setText("");
        mPwdView.getInputEt().setText("");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == mBtnLogin) {
            checkInput();
        } else if (view == mTvRegisterNow) {
            Router.goRegister(getActivity());
        } else if (view == mTvForgetPwd) {
            Router.goForgetPwd(getActivity());
        }
    }

    private void updateLoginBtnStatus() {
        String phone = mUsernameView.getInputEt().getText().toString();
        String password = mPwdView.getInputEt().getText().toString();
        boolean enable = !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password);
        mBtnLogin.setEnabled(enable);
    }

    private void checkInput() {
        String phone = mUsernameView.getInputEt().getText().toString();
        if (TextUtils.isEmpty(phone) || !StringUtils.isPhone(phone)) {
            showToast(R.string.login_input_username);
            return;
        }

        String password = mPwdView.getInputEt().getText().toString();
        if (TextUtils.isEmpty(password)) {
            showToast(R.string.login_input_pwd);
            return;
        }

    }

    public void onSuccess(String authorization) {
        hideProgress();
        // 保存用户登录信息，并更新请求header
        //  GradingUtils.login(authorization);
        // 跳转到主界面
        Router.goMain(getActivity());
    }


}
