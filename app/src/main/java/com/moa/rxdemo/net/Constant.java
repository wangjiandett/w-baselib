package com.moa.rxdemo.net;

/**
 * 常量定义
 *
 * @author wangjian
 * Created on 2020/11/3 11:19
 */
public final class Constant {

    /**
     * rest 接口地址
     */
    public static final String BASE_URL =
                                        // "http://192.168.124.102:8000/";
                                        "http://121.196.14.167:8866/";
                                        // "http://47.114.146.76:9000/";

    /**
     * websocket接口地址
     */
    public static final String SOCKET_URL =
                                        // "ws://121.196.14.167:8899/api/wsst";
                                        "wss://socket.idcd.com:1443";

    /**
     * 成功响应码
     */
    public static final int SUCCESS_CODE = 1;

    /**
     * 未登录响应码
     */
    public static final int NO_LOGIN_CODE = -1;
}
