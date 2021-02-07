package com.moa.rxdemo.mvp.bean;

import java.io.Serializable;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2021/1/31 14:28
 */
public class WebSocketBean implements Serializable {

    public WebSocketBean() {
    }

    public WebSocketBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code;
    public String msg;

}
