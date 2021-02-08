package com.moa.baselib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.moa.baselib.BaseApp;
import com.moa.baselib.base.Config;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2018/12/17 15:53
 */
public class AppUtils {

    /**
     * 更新多语言
     *
     * @param context
     * @return
     */
    public static Context updateConfiguration(Context context) {

        Locale locale = Config.getDefault().getCurrentLocale();

        Configuration config = context.getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            context.getResources().updateConfiguration(config, dm);
        }
        return context;
    }

    public static String getVersionName() {
        PackageInfo info = getPackageInfo();
        if (info != null) {
            return info.versionName;
        }
        return "";
    }

    public static long getVersionCode() {
        PackageInfo info = getPackageInfo();
        if (info != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return info.getLongVersionCode();
            } else {
                return info.versionCode;
            }
        }
        return 0;
    }

    public static String getVersionNameAndCode() {
        return getVersionNameAndCode(".");
    }

    public static String getVersionNameAndCode(String divider) {
        PackageInfo info = getPackageInfo();
        String versionName = "";
        String versionCode = "";
        if (info != null) {
            versionName = info.versionName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = info.getLongVersionCode() + "";
            } else {
                versionCode = info.versionCode + "";
            }
        }

        return versionName + divider + versionCode;
    }

    public static PackageInfo getPackageInfo() {
        try {
            Context context = BaseApp.getAppContext();
            return context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开相机照相
     *
     * @param fragment fragment
     * @param activity activity
     * @param file     文件保存地址
     * @param code     请求码
     */
    public static void openCamera(Fragment fragment, Activity activity, File file, int code) {
        if (file != null) {
            try {
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                if (Build.VERSION.SDK_INT >= 24)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (fragment != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider7.getUriForFile(fragment.getContext(), file));
                    fragment.startActivityForResult(intent, code);
                } else if (activity != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider7.getUriForFile(activity, file));
                    activity.startActivityForResult(intent, code);
                }
            } catch (ActivityNotFoundException e) {
                LogUtils.e(e.getMessage());
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    }

    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static void chooseImage(Fragment fragment, Activity activity, int CROP_REQUEST_CODE) {
//         1.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

//        2.

//        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");

//        3.
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
        if (fragment != null) {
            fragment.startActivityForResult(intent, CROP_REQUEST_CODE);
        } else {
            activity.startActivityForResult(intent, CROP_REQUEST_CODE);
        }
    }

    /**
     * 图片裁剪，可自定义输出宽度高度，宽高比
     *
     * @param uri             图片地址
     * @param scalable        是否可缩放
     * @param outWidth        输出宽度
     * @param outHeight       输出高度
     * @param aspectX         输出宽度比
     * @param aspectY         输出高度比
     * @param outFile         输出文件
     * @param fragment        跳转和接收回调的界面
     * @param activity        跳转和接收回调的界面
     * @param corpRequestCode 请求码
     */
    public static void cropImage(Uri uri, boolean scalable, int outWidth, int outHeight, int aspectX, int aspectY, File outFile, Fragment fragment,
                                 Activity activity, int corpRequestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 加入访问权限
        /*解决跳转到裁剪提示“图片加载失败”问题*/
        // intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(uri, "image/*");
        //显示剪辑的小方框，不然没有剪辑功能，只能选取图
        intent.putExtra("crop", true);
        //是否可以缩放,解决图片有黑边问题
        intent.putExtra("scale", scalable);
        intent.putExtra("scaleUpIfNeeded", true);
        // 设置x,y的比例，截图方框就按照这个比例来截 若设置为0,0，或者不设置 则自由比例截图
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // 裁剪区的宽和高 其实就是裁剪后的显示区域 若裁剪的比例不是显示的比例，
        // 则自动压缩图片填满显示区域。若设置为0,0 就不显示。若不设置，则按原始大小显示
        if (outWidth >= 0) {
            intent.putExtra("outputX", outWidth);
            intent.putExtra("outputY", outHeight);
        }

        // 输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        // 是否将数据保留在Bitmap中返回 此处回调中不在返回图片，而是放入outFile中
        intent.putExtra("return-data", false);
        // Android对Intent中所包含数据的大小是有限制的，一般不能超过1M，
        // 否则应用就会崩溃，这就是Intent中的图片数据只能是缩略图的原因。
        // 而解决的办法也很简单，我们需要给图片裁剪应用指定一个输出文件，用来存放裁剪后的图片
        intent.putExtra("output", Uri.fromFile(outFile));// 裁剪后的保存文件
        if (fragment != null) {
            fragment.startActivityForResult(intent, corpRequestCode);
        } else {
            activity.startActivityForResult(intent, corpRequestCode);
        }
    }


    public static Drawable getDrawable(Context context, int resid) {
        return ContextCompat.getDrawable(context, resid);
    }

    public static int getColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * 判断是否主进程
     *
     * @param context 上下文
     * @return true是主进程
     */
    public static boolean isMainProcess(Context context) {
        return isSameProcess(context, Process.myPid(), getMainProcessName(context));
    }

    /**
     * 获取主进程名
     *
     * @param context 上下文
     * @return 主进程名
     */
    public static String getMainProcessName(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).processName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 是否是相同进程
     *
     * @param context     上下文
     * @param pid         进程id
     * @param processName 进程名
     * @return 是否是相同进程
     */
    public static boolean isSameProcess(Context context, int pid, String processName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (pid == info.pid && TextUtils.equals(info.processName, processName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorId);
        }
    }

    /**
     * 设置底部虚拟导航栏颜色
     */
    public static void setNavigationBarColor(Activity activity, int colorId) {
        // 虚拟导航键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setNavigationBarColor(colorId);
        }
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            LogUtils.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }
}
