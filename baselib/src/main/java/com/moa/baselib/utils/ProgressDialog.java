package com.moa.baselib.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moa.baselib.R;

/**
 * 网络加载等待dialog
 *
 * @author wangjian
 * Created on 2020/8/6 10:38
 */
public class ProgressDialog {

    private View view;
    private Dialog dialog;
    private ProgressBar mProgressbar;
    private ImageView mIvCancel;
    private TextView mTvContent;
    private Context mContext;


    public static ProgressDialog getInstance(Context context) {
        return new ProgressDialog(context);
    }

    private ProgressDialog(Context context) {
        this.mContext = context;

        dialog = new Dialog(context, R.style.tt_progressDialog);
        view = LayoutInflater.from(context).inflate(R.layout.tt_layout_loading_progress, null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        mIvCancel = (ImageView) view.findViewById(R.id.iv_cancel);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(view);
            window.setGravity(Gravity.CENTER);
            //window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setRequestCancelEnable(boolean cancelEnable, View.OnClickListener mOnClickListener) {
        this.mIvCancel.setVisibility(cancelEnable ? View.VISIBLE : View.GONE);
        this.mIvCancel.setOnClickListener(mOnClickListener);
    }

    public void setProgressText(String progressText) {
        mTvContent.setText(progressText);
    }

    public void showProgress() {
        dialog.show();
    }

    public void dismissProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
