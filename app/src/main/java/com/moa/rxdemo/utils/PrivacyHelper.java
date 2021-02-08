package com.moa.rxdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.moa.baselib.base.dispatcher.Runtimes;
import com.moa.baselib.utils.ColorUtils;
import com.moa.baselib.utils.DialogUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.Router;

/**
 * 用户协议和隐私策略帮助类
 *
 * @author wangjian
 * Created on 2020/8/24 15:13
 */
public class PrivacyHelper {

    public static void showPrivacyDialog(Activity activity) {
        Dialog dialog = DialogUtils.showCenterDialog(activity, R.layout.tt_privacy_dialog, false);
        View view = dialog.getWindow().getDecorView();
        TextView tvContent = view.findViewById(R.id.tv_content);
        String content = activity.getString(R.string.user_privacy_privacy_desc, activity.getString(R.string.app_name));
        String userAgree = activity.getString(R.string.user_user_agreement);
        String privacyPolicy = activity.getString(R.string.user_privacy_policy);

        int userAgreeIndex = content.indexOf(userAgree);
        int privacyPolicyIndex = content.indexOf(privacyPolicy);

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(ClickSpan.getUserAgreeSpan(activity), userAgreeIndex, userAgreeIndex + userAgree.length(), 0);
        builder.setSpan(ClickSpan.getPrivacyPolicy(activity), privacyPolicyIndex, privacyPolicyIndex + privacyPolicy.length(), 0);

        tvContent.setText(builder, TextView.BufferType.SPANNABLE);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        view.findViewById(R.id.tv_disagree).setOnClickListener(v -> {
            dialog.dismiss();
            AppConfig.saveAgreePrivacyPolicy(false);
            activity.finish();
        });

        view.findViewById(R.id.btn_agree).setOnClickListener(v -> {
            dialog.dismiss();
            AppConfig.saveAgreePrivacyPolicy(true);
            jump2MainOrGuide(activity);
        });
    }

    public static void jump2MainOrGuide(Activity context){
        Runtimes.dispatchDelay(() -> {
            if (AppConfig.getNeedShowGuide()) {
                Router.goGuide(context);
            } else {
                Router.goMain(context);
            }
            context.finish();
        }, 100);
    }

    private static class ClickSpan extends ClickableSpan {

        private static final int USER_AGREE = 0;
        private static final int PRIVACY_POLICY = 1;

        private int type;
        private Context context;

        public static ClickSpan getUserAgreeSpan(Context context) {
            return new ClickSpan(context, USER_AGREE);
        }

        public static ClickSpan getPrivacyPolicy(Context context) {
            return new ClickSpan(context, PRIVACY_POLICY);
        }

        public ClickSpan(Context context, int type) {
            this.context = context;
            this.type = type;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(ColorUtils.getColor(context, R.color.tt_color_61beff));
        }

        @Override
        public void onClick(@NonNull View widget) {
            // 跳转用户协议
            if (type == USER_AGREE) {
                Router.goUserAgreement(widget.getContext());
            } else if (type == PRIVACY_POLICY) {
                // 跳转隐私策略
                Router.goPrivacyPolicy(widget.getContext());
            }
        }
    }
}
