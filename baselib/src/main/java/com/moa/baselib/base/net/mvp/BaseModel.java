/*
 * Copyright (c) 2018.  For more infomation visit https://github.com/wangjiandett/RxDemo
 */
package com.moa.baselib.base.net.mvp;

import com.moa.baselib.base.net.IBaseResponse;
import com.moa.baselib.base.net.exception.ExceptionHandle;
import com.moa.baselib.base.net.exception.ResponseException;
import com.moa.baselib.base.net.exception.ServerException;
import com.moa.baselib.utils.LogUtils;
import com.moa.baselib.utils.net.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Base model 处理请求，返回响应实体Resp中如果没有Data实体，或想直接处理Resp，可以设置Data实体为Resp
 * <p>
 * Created by：wangjian on 2017/12/21 16:41
 *
 * @param <Resp> 请求响应实体
 * @param <Data> 请求响应实体中的data实体
 */
public abstract class BaseModel<Resp, Data> {

    protected ValueCallback<Data> mCallback;
    private Disposable mDisposable;

    public BaseModel() {
    }

    /**
     * send request and parse data
     *
     * @param observable observable
     */
    protected void request(Observable<Resp> observable) {
        boolean isNetConnected = NetworkUtils.isConnected();
        LogUtils.d("isNetConnected: " + isNetConnected);
        if (!isNetConnected) {
            onHideProgress();
            dealError(new ConnectException());
            return;
        }

        observable.subscribeOn(Schedulers.io())// io线程中执行请求
                .observeOn(AndroidSchedulers.mainThread())// 主线程中执行监听
                .map(new ResultFilter())// 处理响应结果
                .subscribe(new Observer<Data>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        onStart();
                    }

                    protected void onStart() {
                        LogUtils.d("onStart");
                        onShowProgress();
                    }

                    @Override
                    public void onNext(@NotNull Data value) {
                        LogUtils.d("onNext");
                        onHideProgress();
                        onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError:" + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                        onHideProgress();
                        dealError(e);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    /**
     * 取消请求监听
     */
    public void cancel() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    /**
     * 结果处理器
     */
    private class ResultFilter implements Function<Resp, Data> {

        @Override
        public Data apply(Resp baseResponse) {
            // when status equals success code
            if (baseResponse instanceof IBaseResponse) {
                IBaseResponse<Data> response = (IBaseResponse<Data>) baseResponse;
                if (response.isSuccess()) {
                    // 请求成功，但是返回结果为空的情况
                    if (response.getData() == null) {
                        // 获取data实例
                        Data data = getDataInstance();
                        if (data == null) {
                            // 否则返回object
                            data = (Data) new Object();
                        }
                        return data;
                    }
                    return (Data) response.getData();
                } else {
                    // throw data error exception
                    throw new ServerException(response.getCode(), response.getMsg());
                }
            } else {
                return (Data) baseResponse;
            }
        }
    }

    /**
     * 通过反射获取Data类型的实例
     *
     * @return data
     */
    private Data getDataInstance() {
        Data data = null;
        try {
            //this指的是当前运行的实例（子类实例）
            Class clz = this.getClass();
            //2.获取类的泛型父类
            //Type: 是Java里面所有类型的父接口
            //获取泛型父类，必须用该方法，此处的泛型父类不是指当前的类，而是具体继承的BaseAction<Standard>，当前类为BaseAction<T>泛型尚未确定
            Type type = clz.getGenericSuperclass();
            //3.把Type转换为具体的类型
            //将泛型父类转换为具体的那种类型
            ParameterizedType pt = (ParameterizedType) type;
            // 通过反射获取model的真实类型
            Class<Data> clazz = (Class<Data>) pt.getActualTypeArguments()[0];
            // 通过反射创建model的实例
            data = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * deal the exception
     *
     * @param e the exception
     */
    protected void dealError(Throwable e) {
        ResponseException throwable = ExceptionHandle.handleException(e);
        // 以下2个方法，子类执行覆盖一个即可
        onFail(throwable.message);
        // 添加一个带返回error code的回调
        onFail(throwable.code, throwable.message);
    }

    /**
     * show loading progress
     */
    protected void onShowProgress() {
    }

    /**
     * hide loading progress
     */
    protected void onHideProgress() {
    }

    /**
     * the success callback
     *
     * @param value the success value
     */
    protected abstract void onSuccess(Data value);

    /**
     * the fail callback
     *
     * @param msg the fail message
     */
    protected void onFail(String msg){}

    /**
     * the fail callback
     *
     * @param errorCode the error code
     * @param msg the fail message
     */
    protected void onFail(int errorCode, String msg){}


}
