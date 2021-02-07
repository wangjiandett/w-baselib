package com.moa.rxdemo.mvp.model;


import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.CheckUpdateResponse;
import com.moa.rxdemo.mvp.contract.CheckUpdateContract;
import com.moa.rxdemo.net.ApiCreator;
import com.moa.rxdemo.net.AppModel;

/**
 * 检测更新接口请求
 * <p>
 * Created by：wangjian on 2017/12/21 11:00
 */
public class CheckUpdateModel extends AppModel<CheckUpdateResponse> implements CheckUpdateContract.ICheckUpdateModel {

    @Override
    public void checkUpdate(String versionName, ValueCallback<CheckUpdateResponse> callback) {
        this.mCallback = callback;
        request(ApiCreator.apiInterfaces.checkUpdate(versionName));
    }

    @Override
    protected void onShowProgress() {// optional
        mCallback.onShowProgress();
    }
    
    @Override
    protected void onHideProgress() {// optional
        mCallback.onHideProgress();
    }
    
    @Override
    protected void onSuccess(CheckUpdateResponse value) {
        mCallback.onSuccess(value);
    }
    
    @Override
    protected void onFail(String msg) {
        mCallback.onFail(msg);
    }



}
