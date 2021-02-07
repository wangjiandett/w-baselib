package com.moa.rxdemo.mvp.contract;


import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.CheckUpdateResponse;

/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2017/12/21 10:44
 */
public interface CheckUpdateContract {

    interface ICheckUpdateModel {
        void checkUpdate(String versionName, ValueCallback<CheckUpdateResponse> callback);
    }

    interface ICheckUpdatePresenter {
        void checkUpdate(String versionName);
    }

    interface ICheckUpdateView {
        void onSuccess(CheckUpdateResponse response);

        void onFail(String msg);
    }


}
