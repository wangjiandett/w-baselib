package com.moa.baselib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterViewFlipper;

import androidx.annotation.RequiresApi;

/**
 * 重写onTouchEvent返回值，防止无法监听item click事件
 *
 * @author wangjian
 * Created on 2021/1/18 11:12
 */
public class AdapterViewFlipper2 extends AdapterViewFlipper {
    public AdapterViewFlipper2(Context context) {
        super(context);
    }

    public AdapterViewFlipper2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AdapterViewFlipper2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AdapterViewFlipper2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}
