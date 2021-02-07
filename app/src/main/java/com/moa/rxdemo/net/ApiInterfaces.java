package com.moa.rxdemo.net;

import com.moa.rxdemo.mvp.bean.CheckUpdateResponse;
import com.moa.rxdemo.mvp.bean.ForecastBean;
import com.moa.rxdemo.mvp.bean.RegisterRequest;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 统一管理网络请求接口
 *
 * post请求的时候
 * 注解介绍：@File，@FieldMap参数提交的类型是'application/x-www-form-urlencoded'
 * 注解介绍：@Body参数提交的类型是'application/json'
 * 官方介绍：https://square.github.io/retrofit/
 *
 * Created by：wangjian on 2017/12/22 14:40
 */
public interface ApiInterfaces {

    @GET("levelExam/complete")
    Observable<BaseResponse<Boolean>> updateGradingStatusComplete(@QueryMap Map<String, Object> request);

    @POST("register")
    Observable<BaseResponse<String>> register(@Body RegisterRequest request);

    @POST("updatePwd")
    Observable<BaseResponse<Object>> updatePwd(@Body Map<String, String> request);

    @GET("user/info")
    Observable<BaseResponse<UserInfoResponse>> userInfo();

//    @Headers(value = {"Content-type:application/x-www-form-urlencoded;charset=UTF-8"})
//    @Headers(value = {"Content-type:application/json;charset=UTF-8"})
    @POST("cardBanner/list")
    Observable<BaseResponse<List<ForecastBean.DataBean>>> getBannerList(@Body int cityId);

    @GET("checkUpdate")
    Observable<BaseResponse<CheckUpdateResponse>> checkUpdate(@Query("versionName") String versionName);

}
