package com.moa.rxdemo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.moa.baselib.utils.AppUtils;
import com.moa.rxdemo.mvp.bean.UserInfoResponse;
import com.moa.rxdemo.mvp.bean.event.LoginEvent;
import com.moa.rxdemo.net.ApiCreator;
import com.moa.baselib.view.CenteredImageSpan;

import org.greenrobot.eventbus.EventBus;

/**
 * 通用工具类
 *
 * @author wangjian
 * Created on 2020/8/3 9:59
 */
public class GradingUtils {

    //
    // ====================通用函数=========================
    //

    /**
     * 登录成功，用于保存相关登录信息，并更新请求header
     */
    public static void login(UserInfoResponse response) {
        if (response != null) {
            AppConfig.saveLoginInfo(response);
            // 防止重新登录时，header没有刷新
            ApiCreator.updateHeadersInterceptor();
            EventBus.getDefault().postSticky(new LoginEvent(true));
        }
    }

    /**
     * 退出登录，清除相关用户信息
     */
    public static void logout() {
        AppConfig.saveLoginInfo(null);
        // 退出时清除添加的header
        ApiCreator.updateHeadersInterceptor();
//        App.finishAllActivity();
        EventBus.getDefault().postSticky(new LoginEvent(false));
    }

    /**
     * 判断当前用户是否登录
     *
     * @return true or false
     */
    public static boolean hasLogin() {
        return AppConfig.getLoginInfo() != null;
    }

    public static CharSequence html2Convert2String(String html) {
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString(), Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(Html.fromHtml(html).toString());
        }

        return spanned;
    }

    public static CharSequence htmlConvert2String(String html) {
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(html);
        }

        return spanned;
    }

    /**
     * 加载转换html并加载html中的image标签中的图片，需要异步调用
     */
    public static CharSequence htmlConvert2StringWithImg(String html) {
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, new MyImageGetter(), null);
        } else {
            spanned = Html.fromHtml(html, new MyImageGetter(), null);
        }

        return spanned;
    }


    public static ImageSpan getImageSpanFromDrawable(Context context, int drawableResId) {
        Drawable drawable = AppUtils.getDrawable(context, drawableResId);
        // 必须要设置，否则不显示
        // 设置包裹drawable的矩形边框
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return new CenteredImageSpan(drawable);
    }

}
