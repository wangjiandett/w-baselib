package com.moa.rxdemo.net;

import com.moa.rxdemo.mvp.model.UploadResp;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * 统一管理网络请求接口
 */
public interface UploadApiInterfaces {

    @POST("image/upload")
    Observable<BaseResponse<UploadResp>> uploadFile(@Body RequestBody body);

    // "16891/apk/55259F8EF9824AF1BF80106B0E00BCD1.apk"
    // "dmusic/NeteaseCloudMusic_Music_official_7.3.0.1599039901.apk"
    @Streaming
    @GET("16891/apk/55259F8EF9824AF1BF80106B0E00BCD1.apk")
    Observable<ResponseBody> downloadFile();


}
