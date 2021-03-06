package com.moa.baselib.base.net.mvp;

/**
 * simple value callback implementation
 *
 * <p>
 * Created by：wangjian on 2017/12/21 16:41
 */
public abstract class SimpleValueCallback<T> implements ValueCallback<T> {

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

//    @Override
//    public void onSuccess(T response) {
//
//    }

    @Override
    public void onFail(String msg) {

    }

    @Override
    public void onFail(int errorCode, String msg) {

    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void cancel() {

    }
}
