package com.moa.rxdemo.mvp.presenter;

import com.moa.baselib.base.net.mvp.SimpleValueCallback;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.mvp.contract.UserInfoContract;

/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2017/12/21 15:45
 */
public class UserInfoPresenter implements UserInfoContract.IUserInfoPresenter {

    private UserInfoContract.IUserInfoView iUserInfoView;
    private UserInfoContract.IUserInfoModel iUserInfoModel;

    public UserInfoPresenter(UserInfoContract.IUserInfoView iUserInfoView, UserInfoContract.IUserInfoModel iUserInfoModel) {
        this.iUserInfoView = iUserInfoView;
        this.iUserInfoModel = iUserInfoModel;
    }

    @Override
    public void getUserInfo() {
        iUserInfoModel.getUserInfo(new SimpleValueCallback<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse response) {
                iUserInfoView.onGetUserInfoSuccess(response);
            }

            @Override
            public void onFail(String msg) {
                iUserInfoView.onFail(msg);
            }
        });
    }
}
