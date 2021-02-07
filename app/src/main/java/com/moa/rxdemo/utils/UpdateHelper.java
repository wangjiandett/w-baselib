package com.moa.rxdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.utils.DialogUtils;
import com.moa.rxdemo.R;

/**
 * 用户协议和隐私策略帮助类
 *
 * @author wangjian
 * Created on 2020/8/24 15:13
 */
public class UpdateHelper {

    /**
     * 处理检测更新数据，判断是否弹出更新提示对话框，checkSaveStatus一般在自动检测时为true，手动检测为false
     *
     * @param activity activity
     * @param response 响应数据
     * @param checkSaveStatus 是否需要检测 保存的状态
     */
    public static void dealUpdateValue(Activity activity, Object response, boolean checkSaveStatus) {

//        if(response == null || TextUtils.isEmpty(response.url)){
//            return;
//        }

        boolean forceUpdate = false;//response.forceUpdate;

        if(!forceUpdate){
            boolean showUpdateDialog = AppConfig.getShowUpdateDialog();
            if(!showUpdateDialog && checkSaveStatus){
                return;
            }
        }

        Dialog dialog = DialogUtils.showCenterDialog(activity, R.layout.tt_update_dialog, false);
        View view = dialog.getWindow().getDecorView();

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        String content = activity.getString(R.string.user_privacy_privacy_desc, activity.getString(R.string.app_name));

        tvContent.setText(content);

        // 强制刷新时，隐藏取消按钮
        View cancelBtn = view.findViewById(R.id.tv_cancel);
        cancelBtn.setVisibility(forceUpdate ? View.GONE : View.VISIBLE);

        cancelBtn.setOnClickListener(v -> {
            AppConfig.saveShowUpdateDialog(false);
            dialog.dismiss();
        });

        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            AppConfig.saveShowUpdateDialog(false);
            dialog.dismiss();
            AppUtils.openBrowser(v.getContext(), "https://d1.music.126.net/dmusic/NeteaseCloudMusic_Music_official_7.3.0.1599039901.apk");
        });
    }
}
