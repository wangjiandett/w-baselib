package com.moa.rxdemo.mvp.view.my;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.PermissionHelper;
import com.moa.rxdemo.App;
import com.moa.rxdemo.R;
import com.moa.rxdemo.Router;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.mvp.contract.UserInfoContract;
import com.moa.rxdemo.mvp.model.UserInfoModel;
import com.moa.rxdemo.mvp.presenter.UserInfoPresenter;
import com.moa.rxdemo.utils.AppConfig;
import com.moa.rxdemo.utils.ConfirmDialogHelper;
import com.moa.rxdemo.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class MyFragment extends BaseFragment implements UserInfoContract.IUserInfoView {

    private static final int REQ_PERMISSION_CALL_CODE = 1;

    private ScrollView mContainer;
    private RelativeLayout mLlHeader;
    private ImageView mIvHeadIcon;
    private TextView mTvUsername;
    private TextView mTvTel;
    private View mLlEdit;
    private Button mBtnLogin;
    private RelativeLayout mLlShopCar;
    private TextView mTvShopCarNum;
    private LinearLayout mLlReadyPay;
    private LinearLayout mLlRunning;
    private LinearLayout mLlFinish;
    private ImageView mIvInvite;
    private GridView mGvFunctions;

    private UserInfoPresenter mUserInfoPresenter;

    public MyFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_my;
    }

    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);
        setTitle(R.string.my);
        mContainer = findViewById(R.id.container);
        mLlHeader = (RelativeLayout) findViewById(R.id.ll_header);
        mIvHeadIcon = (ImageView) findViewById(R.id.iv_head_icon);
        mTvUsername = (TextView) findViewById(R.id.tv_username);
        mTvTel = (TextView) findViewById(R.id.tv_tel);
        mLlEdit = findViewById(R.id.ll_edit);

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mLlShopCar = (RelativeLayout) findViewById(R.id.ll_shop_car);
        mTvShopCarNum = (TextView) findViewById(R.id.tv_shop_car_num);
        mLlReadyPay = (LinearLayout) findViewById(R.id.ll_ready_pay);
        mLlRunning = (LinearLayout) findViewById(R.id.ll_running);
        mLlFinish = (LinearLayout) findViewById(R.id.ll_finish);
        mIvInvite = (ImageView) findViewById(R.id.iv_invite);
        mGvFunctions = findViewById(R.id.gv_functions);

        setOnClickListener(mLlEdit, mBtnLogin, mLlShopCar, mLlReadyPay, mLlRunning, mLlFinish, mIvInvite);

        mGvFunctions.setOnItemClickListener((parent, view1, position, id) -> {
            FunctionItem item = (FunctionItem) parent.getAdapter().getItem(position);
            switch (item.titleStringRes) {
                case R.string.my_connect_us:
                    boolean hasPermission = PermissionHelper.checkCallPhonePermission(this, getActivity(), REQ_PERMISSION_CALL_CODE);
                    if (hasPermission) {
                        callCustomerServicePhone();
                    }
                    break;
                case R.string.my_send_advice:
                    Router.goSuggestionPage(getActivity());
                    break;
                case R.string.my_about_us:
                    Router.goAbout(getActivity());
                    break;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_CALL_CODE && PermissionHelper.vertifiedPermission(grantResults)) {
            callCustomerServicePhone();
        }
    }

    private void callCustomerServicePhone() {
        ConfirmDialogHelper.showConfirmDialog(getActivity(), "", getString(R.string.call_customer_service_phone, getString(R.string.customer_service_phone)), null, v -> {
            AppUtils.callPhone(getActivity(), getString(R.string.customer_service_phone));
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mUserInfoPresenter = new UserInfoPresenter(this, new UserInfoModel());
        initMyFunctions();

        // 监听用户信息变化
        App.getAppInstance().getUserInfoLiveData().observe(this, userInfoResponse -> updateMyUi());
        // 第一次进来，初始化界面，防止登录按钮不显示
        loadUserInfo();
        updateMyUi();
    }

    // tab页切换时调用
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isAdded() && !isHidden()) {
            setTitle(R.string.my);
            //loadUserInfo();
        }
    }

    // 界面跳转时调用
    @Override
    public void onResume() {
        super.onResume();
        if (isAdded() && !isHidden()) {
            //loadUserInfo();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mLlEdit) {
            Router.goSetting(getActivity());
        } else if (v == mBtnLogin) {
            Router.goLogin(getActivity());
        } else if (v == mLlShopCar) {

        } else if (v == mLlReadyPay) {

        } else if (v == mLlRunning) {

        } else if (v == mLlFinish) {

        } else if (v == mIvInvite) {

        }
    }

    private void initMyFunctions() {
        MyFunctionsAdapter myFunctionsAdapter = new MyFunctionsAdapter(getActivity());
        mGvFunctions.setAdapter(myFunctionsAdapter);

        List<Integer> colors = new ArrayList<Integer>();
        colors.add(R.color.color_F4F5FE);
        colors.add(R.color.color_F3FFFB);
        colors.add(R.color.color_FEFAF4);
        colors.add(R.color.color_FFF3F7);
        colors.add(R.color.color_F6F2FF);
        colors.add(R.color.color_F0F7FF);
        colors.add(R.color.color_FCFBF2);
        colors.add(R.color.color_F4FAFF);

        List<Integer> titles = new ArrayList<Integer>();
        titles.add(R.string.my_info);
        titles.add(R.string.my_enter_center);
        titles.add(R.string.my_report_list);
        titles.add(R.string.my_address);
        titles.add(R.string.my_collection);
        titles.add(R.string.my_connect_us);
        titles.add(R.string.my_send_advice);
        titles.add(R.string.my_about_us);

        List<Integer> logos = new ArrayList<Integer>();
        logos.add(R.mipmap.my_info);
        logos.add(R.mipmap.my_enter_center);
        logos.add(R.mipmap.my_report_list);
        logos.add(R.mipmap.my_address);
        logos.add(R.mipmap.my_collection);
        logos.add(R.mipmap.my_connect_us);
        logos.add(R.mipmap.my_send_advice);
        logos.add(R.mipmap.my_about_us);

        ArrayList<FunctionItem> list = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            FunctionItem item = new FunctionItem();
            item.bgRes = colors.get(i);
            item.titleStringRes = titles.get(i);
            item.logoRes = logos.get(i);
            list.add(item);
        }

        myFunctionsAdapter.setListAndNotify(list);
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoResponse response) {
        // 将登陆接口保存的authorization保存到获取的用户信息中
        UserInfoResponse userInfo = AppConfig.getLoginInfo();
        if (userInfo != null) {
            response.authorization = userInfo.authorization;
            AppConfig.saveLoginInfo(response);

            updateMyUi();
        }
    }

    @Override
    public void onFail(String msg) {
        hideProgress();
        showToast(msg);
    }

    private void loadUserInfo() {
        mUserInfoPresenter.getUserInfo();
    }

    private void updateMyUi() {
        UserInfoResponse response = AppConfig.getLoginInfo();

        if (response != null) {
            mTvUsername.setVisibility(View.VISIBLE);
            mTvTel.setVisibility(View.VISIBLE);
            mLlEdit.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.GONE);

            String nick = TextUtils.isEmpty(response.nick) ? response.mobile : response.nick;
            mTvUsername.setText(nick);
            mTvTel.setText(response.mobile);

            ImageUtils.loadCircleImg(response.avatar, mIvHeadIcon, R.mipmap.avatar_placeholder_round);

        } else {
            mTvUsername.setVisibility(View.GONE);
            mTvTel.setVisibility(View.GONE);
            mLlEdit.setVisibility(View.GONE);
            mBtnLogin.setVisibility(View.VISIBLE);

            ImageUtils.loadCircleImg("", mIvHeadIcon, R.mipmap.avatar_placeholder_round);
        }
    }

}
