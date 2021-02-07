package com.moa.rxdemo.mvp.model;

import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.BannersResponse;
import com.moa.rxdemo.mvp.contract.BannersContract;
import com.moa.rxdemo.net.AppModel;

import java.util.List;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/9/30 17:12
 */
public class BannersModel extends AppModel<List<BannersResponse>> implements BannersContract.IBannersModel {

    @Override
    public void sendBannersRequest(ValueCallback<List<BannersResponse>> callback) {
        this.mCallback = callback;
        //request(ApiCreator.apiInterfaces.bannerList());
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
    protected void onSuccess(List<BannersResponse> value) {
        mCallback.onSuccess(value);
    }

    @Override
    protected void onFail(String msg) {
        mCallback.onFail(msg);
    }

}
