package com.moa.rxdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.moa.baselib.utils.DialogUtils;
import com.moa.rxdemo.R;

/**
 * confirm dialog 帮助类
 *
 * @author wangjian
 * Created on 2020/8/24 15:13
 */
public class ConfirmDialogHelper {

    public static Dialog showConfirmDialog(Activity activity, int resIdTitle, int resIdContent, View.OnClickListener onCancelClickListener, View.OnClickListener onOkClickListener) {
        String title = "";
        if(resIdTitle > 0){
            title = activity.getString(resIdTitle);
        }
        return showConfirmDialog(activity, title,  activity.getString(resIdContent), onCancelClickListener, onOkClickListener);
    }

    public static Dialog showConfirmDialog(Activity activity, String stringTitle, String stringContent, View.OnClickListener onCancelClickListener, View.OnClickListener onOkClickListener) {
        Dialog dialog = DialogUtils.showCenterDialog(activity, R.layout.tt_confirm_dialog, false);
        View view = dialog.getWindow().getDecorView();

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        View cancelBtn = view.findViewById(R.id.tv_cancel);

        // 有title时才显示
        if(!TextUtils.isEmpty(stringTitle)){
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(stringTitle);
        }

        tvContent.setText(stringContent);

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if(onCancelClickListener != null){
                onCancelClickListener.onClick(v);
            }
        });

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dialog.dismiss();
            if(onOkClickListener != null){
                onOkClickListener.onClick(v);
            }
        });

        return dialog;
    }


    /**
     * show confirm dialog
     *
     * @param activity activity
     * @param stringResId 显示的文本
     * @param onCancelClickListener 取消按钮点击
     * @param onOkClickListener 确定按钮点击
     * @return dialog
     */
    public static Dialog showConfirmDialog(Activity activity, int stringResId, View.OnClickListener onCancelClickListener, View.OnClickListener onOkClickListener) {
        return showConfirmDialog(activity, -1, stringResId, onCancelClickListener, onOkClickListener);
    }
}
