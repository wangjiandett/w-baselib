package com.moa.rxdemo.net;

import android.text.TextUtils;

import com.moa.baselib.base.net.ApiService;
import com.moa.baselib.base.net.DownloadInterceptor;
import com.moa.baselib.base.net.HeaderParamsInterceptor;
import com.moa.baselib.base.net.ProgressResultListener;
import com.moa.baselib.base.net.UploadInterceptor;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.utils.AppConfig;

import java.util.Map;

/**
 * data creator helper
 * <p>
 * Created by：wangjian on 2018/8/21 15:11
 */
public class ApiCreator {

    /**
     * 添加共用header信息
     */
    private static final HeaderParamsInterceptor mHeadersInterceptor = new HeaderParamsInterceptor.Builder().build();

    /**
     * 接口列表
     */
    public static ApiInterfaces apiInterfaces = getApiService().getApis(ApiInterfaces.class);

    /**
     * net request config
     */
    private static ApiService getApiService() {
        return new ApiService //
                .Builder() //
                .interceptor(updateHeadersInterceptor()) //
                .baseUrl(getBaseUrl()) //
                .build();
    }

    private static String getBaseUrl() {
        return AppConfig.getLocalBaseUrl();
    }

    /**
     * 添加请求头
     */
    public static HeaderParamsInterceptor updateHeadersInterceptor() {
        String headerKey = "Authorization";
        Map<String, String> headerMap = mHeadersInterceptor.getHeaderParamsMap();
        UserInfoResponse response = AppConfig.getLoginInfo();
        if (response != null && !TextUtils.isEmpty(response.authorization)) {
            headerMap.put(headerKey, response.authorization);
        } else {
            headerMap.remove(headerKey);
        }

        return mHeadersInterceptor;
    }

    /**
     * 下载文件接口
     *
     * @param listener 进度监听
     */
    public static UploadApiInterfaces getDownloadFileInterface(ProgressResultListener listener) {
        ApiService test = new ApiService.Builder()
                .baseUrl("https://imtt.dd.qq.com/") // "https://imtt.dd.qq.com/"  "https://d1.music.126.net/"
                // 监听下载进度，需要设置拦截器
                .netInterceptor(new DownloadInterceptor(listener))
                .interceptor(updateHeadersInterceptor())
                .build();
        return test.getApis(UploadApiInterfaces.class);
    }

    /**
     * 上传文件接口
     *
     * @param listener 进度监听
     */
    public static UploadApiInterfaces getUploadFileInterface(ProgressResultListener listener) {
        ApiService test = new ApiService.Builder()
                .baseUrl(getBaseUrl())
                // 监听下载进度，需要设置拦截器
                .netInterceptor(new UploadInterceptor(listener))
                .interceptor(updateHeadersInterceptor())
                .build();
        return test.getApis(UploadApiInterfaces.class);
    }


}
