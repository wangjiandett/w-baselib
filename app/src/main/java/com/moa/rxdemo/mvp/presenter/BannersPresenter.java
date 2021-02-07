package com.moa.rxdemo.mvp.presenter;

import com.moa.baselib.base.net.mvp.SimpleValueCallback;
import com.moa.rxdemo.mvp.bean.BannersResponse;
import com.moa.rxdemo.mvp.contract.BannersContract;

import java.util.List;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/9/30 17:12
 */
public class BannersPresenter implements BannersContract.IBannersPresenter {

    private BannersContract.IBannersView iView;
    private BannersContract.IBannersModel iModel;

    public BannersPresenter(BannersContract.IBannersView iView, BannersContract.IBannersModel iModel) {
        this.iView = iView;
        this.iModel = iModel;
    }

    @Override
    public void sendBannersRequest() {
        iModel.sendBannersRequest(new SimpleValueCallback<List<BannersResponse>>() {
            @Override
            public void onSuccess(List<BannersResponse> response) {
                iView.onBannersSuccess(response);
            }

            @Override
            public void onFail(String msg) {
                iView.onFail(msg);
            }
        });
    }

}
