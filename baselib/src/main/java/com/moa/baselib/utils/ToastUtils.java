package com.moa.baselib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moa.baselib.R;

/**
 * ToastUtils，封装单例toast防止重复创建，支持自定义view
 *
 * @author wangjian
 */
public class ToastUtils {

    private static Toast toast;


    private static void getBottomToast(Context context, CharSequence text, int stringRes, int duration) {
        getToast(context, text, stringRes, duration, Gravity.BOTTOM);
    }

    private static void getToast(Context context, CharSequence text, int stringRes, int duration) {
        getToast(context, text, stringRes, duration, Gravity.CENTER);
    }

    /**
     * 使用系统界面的toast
     *
     * @param context   上下文
     * @param text      显示的字符串
     * @param stringRes 显示的字符串资源
     * @param duration  显示时间
     * @param gravity   显示位置
     */
    @SuppressLint("ShowToast")
    private static void getToast(Context context, CharSequence text, int stringRes, int duration, int gravity) {
        if (context == null) {
            LogUtils.d("the context can not be null");
            return;
            // throw new NullPointerException("the context can not be null");
        }

        getCustomUIToast(context, text, stringRes, duration, gravity);

//        if (toast == null) {
//            if (!TextUtils.isEmpty(text)) {
//                toast = Toast.makeText(context.getApplicationContext(), text, duration);
//            } else {
//                toast = Toast.makeText(context.getApplicationContext(), stringRes, duration);
//            }
//
//            toast.setGravity(gravity, 0, 10);
//        } else {
//            if (!TextUtils.isEmpty(text)) {
//                toast.setText(text);
//             } else if (stringRes > 0) {
//                toast.setText(stringRes);
//            }
//        }
//        toast.show();
    }

    /**
     * 自定义界面的toast
     *
     * @param context  上下文
     * @param text     显示的字符串
     * @param res      显示的字符串资源
     * @param duration 显示时间
     * @param gravity  显示的位置
     */
    private static void getCustomUIToast(Context context, CharSequence text, int res, int duration, int gravity) {

        // 自定义布局，此处暂时使用系统界面，需要在此处修改
        int layoutId = R.layout.tt_layout_toast;

        TextView tvContent = null;
        View view = null;

        if (toast == null) {
            toast = new Toast(context);
            toast.setDuration(duration);
            toast.setGravity(gravity, 0, 0);
            LayoutInflater inflate = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflate.inflate(layoutId, null);
            toast.setView(view);
        }
        view = toast.getView();
        tvContent = view.findViewById(R.id.tv_content);

        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        } else if (res > 0) {
            tvContent.setText(res);
        }
        toast.show();
    }

    /**
     * 显示自定义布局 toast
     *
     * @param context context
     * @param layoutId 布局id
     * @param text 显示文本
     * @param duration 显示时间
     * @param gravity 显示位置
     */
    public static void showCustomUIToast(Context context, int layoutId, CharSequence text, int duration, int gravity) {
        // 自定义布局，此处暂时使用系统界面，需要在此处修改
        // int layoutId = R.layout.tt_layout_toast;
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(layoutId, null);
        showCustomUIToast(context, view, text, duration, gravity);
    }

    public static void showCustomUIToast(Context context, View layout, CharSequence text, int duration, int gravity) {
        // 自定义布局，此处暂时使用系统界面，需要在此处修改
        // int layoutId = R.layout.tt_layout_toast;
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(gravity, 0, 0);
        toast.setView(layout);
        TextView tvContent = layout.findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(text)) {
            tvContent.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, CharSequence msg) {
        getToast(context, msg, 0, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        getToast(context, null, resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        getToast(context, msg, 0, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, int resId) {
        getToast(context, null, resId, Toast.LENGTH_LONG);
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
