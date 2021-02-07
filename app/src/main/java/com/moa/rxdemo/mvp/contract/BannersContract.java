package com.moa.rxdemo.mvp.contract;

import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.mvp.bean.BannersResponse;

import java.util.List;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/9/30 17:12
 */
public interface BannersContract {

    interface IBannersModel {
        void sendBannersRequest(ValueCallback<List<BannersResponse>> callback);
    }

    interface IBannersPresenter {
        void sendBannersRequest();
    }

    interface IBannersView {
        void onBannersSuccess(List<BannersResponse> response);
        void onFail(String msg);
    }
}
