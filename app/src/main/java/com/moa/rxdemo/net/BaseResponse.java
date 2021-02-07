package com.moa.rxdemo.net;

import com.google.gson.annotations.SerializedName;
import com.moa.baselib.base.net.IBaseResponse;

/**
 * Net response
 * <p>
 * Created by：wangjian on 2017/12/20 16:12
 */
public class BaseResponse<T> implements IBaseResponse<T> {

    public T data;
    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isSuccess() {
        return getCode() == Constant.SUCCESS_CODE;
    }
}
