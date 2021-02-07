package com.moa.baselib.base.net;

/**
 * IBaseResponse
 *
 * @author wangjian
 * Created on 2020/11/2 14:24
 */
public interface IBaseResponse<T> {

    int getCode();

    String getMsg();

    T getData();

    boolean isSuccess();

}
