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
    private static HeaderParamsInterceptor mHeadersInterceptor = new HeaderParamsInterceptor.Builder().build();

    /**
     * net request config
     */
    private static final ApiService APISERVICE = new ApiService.Builder().interceptor(updateHeadersInterceptor()).baseUrl(Constant.BASE_URL).build();

    /**
     * 接口列表
     */
    public static ApiInterfaces apiInterfaces = APISERVICE.getApis(ApiInterfaces.class);

    /**
     * 拼接图片地址
     *
     * @param imgSubUrl 服务器返回的sub图片地址
     * @return 完整的图片地址
     */
    public static String getImageUrl(String imgSubUrl) {
        return Constant.BASE_IMG_URL + imgSubUrl;
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
                .baseUrl(Constant.BASE_URL)
                // 监听下载进度，需要设置拦截器
                .netInterceptor(new UploadInterceptor(listener))
                .interceptor(updateHeadersInterceptor())
                .build();
        return test.getApis(UploadApiInterfaces.class);
    }


}
