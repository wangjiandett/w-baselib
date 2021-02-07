package com.moa.rxdemo.net;

import com.moa.baselib.base.net.exception.ExceptionHandle;
import com.moa.baselib.base.net.exception.ResponseException;
import com.moa.baselib.base.net.mvp.BaseModel;
import com.moa.rxdemo.App;
import com.moa.rxdemo.Router;

/**
 * app model 重写部分方法，自定义处理登录失败的情况
 *
 * @author wangjian
 * Created on 2020/10/4 11:23
 */
public abstract class AppModel<Data> extends BaseModel<BaseResponse<Data>, Data> {

    @Override
    protected void dealError(Throwable e) {
        ResponseException throwable = ExceptionHandle.handleException(e);
        // 未登录时
        if (throwable.code == Constant.NO_LOGIN_CODE) {
            Router.goLogin(App.getAppContext());
        }
        super.dealError(e);
    }
}