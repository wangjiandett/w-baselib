package com.moa.rxdemo.mvp.contract;


import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;

/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2017/12/21 10:44
 */
public interface UserInfoContract {

    interface IUserInfoModel {
        void getUserInfo(ValueCallback<UserInfoResponse> callback);
    }

    interface IUserInfoPresenter{
        void getUserInfo();
    }
    
    interface IUserInfoView{
        void onGetUserInfoSuccess(UserInfoResponse response);
        void onFail(String msg);
    }
}
