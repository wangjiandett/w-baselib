package com.moa.baselib.base.net;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 自定义公共请求参数
 *
 * @author wangjian
 * Created on 2020/9/9 14:53
 */
public class BasicParamsInterceptor implements Interceptor {

    private Map<String, String> queryParamsMap = new HashMap<>();
    private Map<String, String> paramsMap = new HashMap<>();

    private BasicParamsInterceptor() {

    }

    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        // process queryParams inject whatever it's GET or POST
        if (queryParamsMap.size() > 0) {
            injectParamsIntoUrl(request, requestBuilder, queryParamsMap);
        }
        // process queryParams end


        // process post body inject
        if (request.method().equals("POST")) {
            RequestBody requestBody = request.body();
            Headers.Builder headerBuilder = request.headers().newBuilder();

            String header = headerBuilder.get("Content-type");

            if (requestBody != null) {
                // 表单提交时
                if (requestBody.contentType() != null
                        && requestBody.contentType().subtype() != null
                        && TextUtils.equals(requestBody.contentType().subtype(), "x-www-form-urlencoded")
                        || (!TextUtils.isEmpty(header) && header.contains("x-www-form-urlencoded"))) {

                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    if (paramsMap.size() > 0) {
                        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                            formBodyBuilder.add(entry.getKey(), entry.getValue());
                        }
                    }
                    RequestBody formBody = formBodyBuilder.build();
                    String postBodyString = bodyToString(request.body());
                    postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                    requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));

                }
                // json格式提交时
                else if (requestBody.contentType() != null
                        && requestBody.contentType().subtype() != null
                        && TextUtils.equals(requestBody.contentType().subtype(), "json")
                        || (!TextUtils.isEmpty(header) && header.contains("json"))) {
                    String str = bodyToString(requestBody);
                    // todo 封装公共参数
                }
            }
        } else {
            // can't inject into body, then inject into url
            injectParamsIntoUrl(request, requestBuilder, paramsMap);
        }

        request = requestBuilder.build();
        return chain.proceed(request);
    }

    // func to inject params into url
    private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if (paramsMap.size() > 0) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        requestBuilder.url(httpUrlBuilder.build());
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            interceptor = new BasicParamsInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return interceptor;
        }

    }
}