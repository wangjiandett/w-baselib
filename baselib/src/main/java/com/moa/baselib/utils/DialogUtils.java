package com.moa.baselib.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.moa.baselib.R;

/**
 * 自定义dialog显示
 * <p>
 * Created by：wangjian on 2017/12/27 11:24
 */
public class DialogUtils {

    /**
     * 底部显示dialog
     *
     * @param context
     */
    public static Dialog showBottomDialog(Context context, int layoutResId, boolean cancelable) {
        Dialog dialog = getDialog(context, layoutResId, Gravity.BOTTOM, cancelable);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.tt_AnimBottom);
        }
        dialog.show();
        return dialog;
    }

    /**
     * 中间显示dialog
     *
     * @param context
     */
    public static Dialog showCenterDialog(Context context, int layoutResId, boolean cancelable) {
        return showDialog(context, layoutResId, Gravity.CENTER, cancelable);
    }

    /**
     * 显示dialog
     *
     * @param context
     */
    public static Dialog showDialog(Context context, int layoutResId, int gravity, boolean cancelable) {
        Dialog dialog = getDialog(context, layoutResId, gravity, cancelable);
        dialog.show();
        return dialog;
    }

    public static Dialog getDialog(Context context, int layoutResId, int gravity, boolean cancelable) {
        return getDialog(context, layoutResId, gravity, false, cancelable);
    }

    public static Dialog getDialog(Context context, int layoutResId, int gravity, boolean fullScreen, boolean cancelable) {
        if (context == null || layoutResId <= 0) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.tt_progressDialog);
        View view = LayoutInflater.from(context).inflate(layoutResId, null);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(view);
            window.setGravity(gravity);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // 设置dialog全屏
            if (fullScreen) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setBackgroundDrawableResource(R.color.transparent);
                window.setAttributes(params);

            } else {
                // 设置dialog距离屏幕的左右间距
                window.getDecorView().setPadding(DisplayUtils.dip2px(50), DisplayUtils.dip2px(25), DisplayUtils.dip2px(50), DisplayUtils.dip2px(25));
            }
        }

        return dialog;
    }

    /**
     * 自定义显示dialog，宽度铺满，高度自定义
     *
     * @param layoutResId 布局文件
     * @param gravity 显示位置
     * @param height 自定义高度
     * @param cancelable 是否自动取消
     */
    public static Dialog getFullWithDiyHeightDialog(Context context, int layoutResId, int gravity, int height, boolean cancelable) {
        if (context == null || layoutResId <= 0) {
            return null;
        }
        Dialog dialog = new Dialog(context, R.style.tt_progressDialog);
        View view = LayoutInflater.from(context).inflate(layoutResId, null);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(view);
            window.setGravity(gravity);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, height);
        }

        return dialog;
    }
}
