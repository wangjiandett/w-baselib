package com.moa.rxdemo.mvp.bean.event;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/9/17 9:54
 */
public class LoginEvent {
    public boolean loginSuccess;

    public LoginEvent() {
    }

    public LoginEvent(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
