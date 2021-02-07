package com.moa.rxdemo.mvp.model;


import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.mvp.contract.UserInfoContract;
import com.moa.rxdemo.net.ApiCreator;
import com.moa.rxdemo.net.AppModel;

/**
 * 获取用户信息接口请求
 * <p>
 * Created by：wangjian on 2017/12/21 11:00
 */
public class UserInfoModel extends AppModel<UserInfoResponse> implements UserInfoContract.IUserInfoModel {

    @Override
    public void getUserInfo(ValueCallback<UserInfoResponse> callback) {
        this.mCallback = callback;
        request(ApiCreator.apiInterfaces.userInfo());
    }

    @Override
    protected void onSuccess(UserInfoResponse value) {
        mCallback.onSuccess(value);
    }
    
    @Override
    protected void onFail(String msg) {
        mCallback.onFail(msg);
    }



}
