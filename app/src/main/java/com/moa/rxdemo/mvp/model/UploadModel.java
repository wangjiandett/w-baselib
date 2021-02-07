package com.moa.rxdemo.mvp.model;

import com.moa.rxdemo.net.BaseResponse;
import com.moa.baselib.base.net.mvp.BaseUploadFileModel;
import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.rxdemo.net.ApiCreator;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 上传文件接口请求
 * <p>
 * Created by：wangjian on 2017/12/21 11:00
 */
public class UploadModel extends BaseUploadFileModel<BaseResponse<UploadResp>, UploadResp> {

    ValueCallback<UploadResp> valueCallback;

    @Override
    public void uploadFile(File file) {
        super.uploadFile("images", file);
    }

    public UploadModel(ValueCallback<UploadResp> valueCallback) {
        this.valueCallback = valueCallback;
    }

    @Override
    public Observable<BaseResponse<UploadResp>> getUploadObservable(RequestBody body) {
        return ApiCreator.getUploadFileInterface(this).uploadFile(body);
    }

    @Override
    public void onProgressChange(int percent) {
        valueCallback.onProgressChange(percent);
    }

    @Override
    protected void onSuccess(UploadResp value) {
        valueCallback.onSuccess(value);
    }

    @Override
    protected void onFail(String msg) {
        valueCallback.onFail(msg);
    }
}
