package com.moa.rxdemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.moa.baselib.base.dispatcher.Runtimes;
import com.moa.baselib.utils.CallBack;
import com.moa.baselib.utils.DisplayUtils;
import com.moa.baselib.utils.FileUtil;
import com.moa.rxdemo.App;
import com.moa.rxdemo.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Glide图片加载显示封装类
 *
 * @author wangjian
 * Created on 2020/8/4 9:18
 */
public class ImageUtils {

    private static int PLACEHOLDER_USER_HEADER = R.mipmap.avatar_placeholder_rectangle;
    private static int PLACEHOLDER_NORMAL = R.mipmap.logo;

    /**
     * 显示6dp圆角
     *
     * @param url       url
     * @param imageView imageView
     */
    public static void loadImgCorner6dp(String url, ImageView imageView) {
        loadCornerImg(url, DisplayUtils.dip2px(6), imageView, PLACEHOLDER_NORMAL);
    }

    public static void loadImgCorner10dp(String url, ImageView imageView) {
        loadCornerImg(url, DisplayUtils.dip2px(10), imageView, PLACEHOLDER_NORMAL);
    }

    public static void loadImgCorner10dp(String url, ImageView imageView, int placeholder) {
        loadCornerImg(url, DisplayUtils.dip2px(10), imageView, placeholder);
    }

    /**
     * 加载用户头像使用
     *
     * @param imgUrl    图片地址
     * @param imageView 显示的位置
     */
    public static void loadHeadIcon(String imgUrl, ImageView imageView) {
        loadImg(imgUrl, imageView.getWidth(), imageView.getHeight(), imageView, PLACEHOLDER_USER_HEADER);
    }

    /**
     * 加载图片
     *
     * @param imgUrl    图片地址
     * @param radius    圆角大小 the corner radius (in device-specific pixels).
     * @param imageView 显示图片的image view
     */
    public static void loadCornerImg(String imgUrl, int radius, ImageView imageView, int placeholder) {
        if (imageView == null) {
            return;
        }

        RoundedCorners roundedCorners = new RoundedCorners(radius);
        RequestOptions options = getGlideOption(imageView.getWidth(), imageView.getHeight(), placeholder)
                .transforms(new CenterCrop(), roundedCorners);
        Glide.with(App.getAppContext()).load(imgUrl).apply(options).into(imageView);
    }

    public static void loadCornerImg(int imageRes, int radius, ImageView imageView) {
        if (imageView == null) {
            return;
        }

        RoundedCorners roundedCorners = new RoundedCorners(radius);
        RequestOptions options = getGlideOption(imageView.getWidth(), imageView.getHeight(), PLACEHOLDER_NORMAL)
                .transforms(new CenterCrop(), roundedCorners);
        Glide.with(App.getAppContext()).load(imageRes).apply(options).into(imageView);
    }

    /**
     * 显示图片
     *
     * @param imgUrl    图片地址
     * @param imageView 显示图片
     */
    public static void loadImg(String imgUrl, ImageView imageView) {
        loadImg(imgUrl, imageView.getWidth(), imageView.getHeight(), imageView, PLACEHOLDER_NORMAL);
    }

    /**
     * 显示图片
     *
     * @param imgUrl    图片地址
     * @param imageView 显示图片
     */
    public static void loadImg(String imgUrl, ImageView imageView, int placeholder) {
        loadImg(imgUrl, imageView.getWidth(), imageView.getHeight(), imageView, placeholder);
    }

    /**
     * 加载显示图片
     *
     * @param imgUrl    图片地址
     * @param width     图片显示宽度
     * @param height    图片显示高度
     * @param imageView 显示的图片
     */
    public static void loadImg(String imgUrl, int width, int height, ImageView imageView, int placeholder) {
        if (imageView == null) {
            return;
        }
        Glide.with(App.getAppContext())
                .load(imgUrl)
                .apply(getGlideOption(width, height, placeholder))
                .into(imageView);
    }

    /**
     * 加载显示资源文件图片
     *
     * @param resourceId 图片地址
     * @param imageView  显示的图片
     */
    public static void loadImg(int resourceId, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(App.getAppContext())
                .load(resourceId)
                .into(imageView);
    }

    public static void loadImgFile(File file, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Glide.with(App.getAppContext())
                .load(file)
                .into(imageView);
    }

    /**
     * 显示圆形图片
     *
     * @param url       图片地址
     * @param imageView 显示图片
     */
    public static void loadCircleImg(String url, ImageView imageView) {
        loadCircleImg(url, imageView, PLACEHOLDER_NORMAL);
    }

    /**
     * 显示圆形图片
     *
     * @param url       图片地址
     * @param imageView 显示图片
     */
    public static void loadCircleImg(String url, ImageView imageView, int placeholder) {
        if (imageView == null) {
            return;
        }

        RequestOptions options = getGlideOption(imageView.getWidth(), imageView.getHeight(), placeholder)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()));
        Glide.with(App.getAppContext()).load(url)
                .apply(options)
                .into(imageView);
    }


    /**
     * 加载图片并转换成bitmap，使用SimpleTarget需要自定义宽度和高度
     *
     * @param url    url
     * @param target target
     */
    public static void loadAsBitmap(String url, SimpleTarget<Bitmap> target) {
        Glide.with(App.getAppContext())
                .asBitmap()
                .load(url)
                .into(target);
    }

    public static void loadAsBitmap(Context context, String url, CallBack<Bitmap> callback) {
        Runtimes.dispatchNow(new Runnable() {
            @Override
            public void run() {
                FutureTarget<Bitmap> target = null;
                RequestManager requestManager = Glide.with(context);
                try {
                    target = requestManager
                            .asBitmap()
                            .load(url)
                            .submit();
                    Bitmap downloadedFile = target.get();
                    callback.onBack(downloadedFile);
                    // ... do something with the file (usually throws IOException)
                } catch (ExecutionException | InterruptedException e) {
                    // ... bug reporting or recovery
                } finally {
                    // make sure to cancel pending operations and free resources
                    if (target != null) {
                        target.cancel(true); // mayInterruptIfRunning
                    }
                }
            }
        });
    }

    /**
     * 下载文件
     *
     * @param context  context
     * @param url      文件地址
     * @param callback 回调
     */
    public static void loadAsFile(Context context, String url, CallBack<File> callback) {
        // 此处需要放到异步线程中执行
        Runtimes.dispatchNow(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> target = null;
                RequestManager requestManager = Glide.with(context);
                try {
                    target = requestManager
                            .downloadOnly()
                            .load(url)
                            .submit();
                    File downloadedFile = target.get();
                    // ... do something with the file (usually throws IOException)
                    if (!TextUtils.isEmpty(url)) {
                        // 对文件重命名并返回
                        String filename = url.substring(url.lastIndexOf("/") + 1);
                        downloadedFile = FileUtil.renameFile(downloadedFile, filename);
                        callback.onBack(downloadedFile);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    // ... bug reporting or recovery
                } finally {
                    // make sure to cancel pending operations and free resources
                    if (target != null) {
                        target.cancel(true); // mayInterruptIfRunning
                    }
                }
            }
        });
    }

    private static RequestOptions getGlideOption(int width, int height, int placeholder) {
        return RequestOptions.placeholderOf(placeholder)
                .error(placeholder)
                .override(width, height);
    }


}
