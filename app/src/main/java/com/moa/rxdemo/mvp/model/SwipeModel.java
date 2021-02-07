package com.moa.rxdemo.mvp.model;


import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.ForecastBean;
import com.moa.rxdemo.net.ApiCreator;
import com.moa.rxdemo.net.AppModel;

import java.util.List;

/**
 * load swipe list
 * <p>
 * Created by：wangjian on 2017/12/21 11:00
 */
public class SwipeModel extends AppModel<List<ForecastBean.DataBean>> implements ISwipeModel {
    
    @Override
    public void loadSwipeList(int cityId, ValueCallback<List<ForecastBean.DataBean>> callback) {
        this.mCallback = callback;
        request(ApiCreator.apiInterfaces.getBannerList(1));
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
    protected void onSuccess(List<ForecastBean.DataBean> value) {
        mCallback.onSuccess(value);
    }
    
    @Override
    protected void onFail(String msg) {
        mCallback.onFail(msg);
    }


}
