package com.moa.baselib.view.auto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/12/26 15:21
 */
public class AutoHeightWebView extends WebView {

    @SuppressLint("NewApi")
    public AutoHeightWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoHeightWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoHeightWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightWebView(Context context) {
        super(context);
    }

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //     int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    //     super.onMeasure(widthMeasureSpec, mExpandSpec);
    // }
}
