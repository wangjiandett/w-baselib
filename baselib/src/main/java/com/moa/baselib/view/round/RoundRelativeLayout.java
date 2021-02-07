/*
 *  Copyright (C) 2016-2017 浙江海宁山顶动力网络科技有限公司
 * 文件说明：
 *  <p>
 *  更新说明:
 *
 *  @author wangjian@xiaokebang.com
 *  @version 1.0.0
 *  @create 17-7-17 下午3:25
 *  @see <a href="http://www.top4s.net/">http://www.top4s.net/</a>
 */
package com.moa.baselib.view.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.moa.baselib.R;
import com.moa.baselib.utils.ColorUtils;


/**
 * 圆角RelativeLayout
 */
public class RoundRelativeLayout extends RelativeLayout {

    public RoundRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private RoundedColorDrawable mRoundBg;
    private ColorDrawable mColorDrawable;
    private int mRadius;
    private int borderWidth;
    private int borderColor;
    private boolean showPressBgColor;

    private void init(Context context, AttributeSet attrs) {
        Drawable bg = getBackground();
        if (bg == null) {
            bg = new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent));
        }

        if (bg instanceof StateListDrawable) {
            // TODO support StateListDrawable
            // mRoundBg = new RoundedColorDrawable(radius, color);
//            StateListDrawable out = new StateListDrawable();
//            StateListDrawable sld = (StateListDrawable) bg;
//            int[] st = sld.getState();
//            for (int i = 0; i < st.length; i++) {
//                sld.selectDrawable(i);
//                sld.getCurrent();
//                
//            }
        } else if (bg instanceof ColorDrawable) {
            mColorDrawable = (ColorDrawable) bg;
            mRoundBg = RoundedColorDrawable.fromColorDrawable(mColorDrawable);
        }
        if (attrs != null) {
            int[] attr = R.styleable.RoundButton;
            TypedArray a = context.obtainStyledAttributes(attrs, attr);
            mRadius = a.getDimensionPixelOffset(R.styleable.RoundButton_android_radius, mRadius);
            borderColor = a.getColor(R.styleable.RoundButton_bordersColor, Color.TRANSPARENT);
            borderWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_bordersWidth, borderWidth);
            // pressColor = a.getColor(R.styleable.RoundButton_pressedBgColor, pressColor);
            showPressBgColor = a.getBoolean(R.styleable.RoundButton_showPressBgColor, showPressBgColor);

            setRadius(mRadius);
            setBorder(borderColor, borderWidth);
            // if (a.hasValue(R.styleable.RoundButton_pressedBgColor)) {
            //     setPressedBgColor(pressColor);
            // }
//            if (a.hasValue(R.styleable.RoundButton_checkedBgColor)) {
//                setStateBgColor(new int[]{android.R.attr.state_checked},
//                    a.getColor(R.styleable.RoundButton_checkedBgColor, Color.TRANSPARENT), borderColor);
//            }
//            if (a.hasValue(R.styleable.RoundButton_selectedBgColor)) {
//                setStateBgColor(new int[]{android.R.attr.state_selected},
//                    a.getColor(R.styleable.RoundButton_selectedBgColor, Color.TRANSPARENT), borderColor);
//            }
            a.recycle();
        }

        if (mColorDrawable != null) {
            setRoundBackground(mColorDrawable);
        }
    }

    private void setDefaultPressColor(int bgColor) {
        // 如果背景是透明色，不转换--转换透明色会变成黑色
        if (showPressBgColor && bgColor != Color.TRANSPARENT) {
            int darkColor = ColorUtils.getDarkerColor(bgColor);
            setPressedBgColor(darkColor);
            apply();
        }
    }

    public void setRadius(int radius) {
        if (mRoundBg != null) {
            mRoundBg.setRadius(radius);
        }
    }

    public void setBorder(int color, int width) {
        if (mRoundBg != null) {
            mRoundBg.setBorder(color, width);
        }
    }

    public void setRoundBackground(Drawable background) {
        if (background instanceof ColorDrawable) {
            mRoundBg = RoundedColorDrawable.fromColorDrawable((ColorDrawable) background);
            mRoundBg.setRadius(mRadius);
            ViewCompat.setBackground(this, mRoundBg);
            setDefaultPressColor(mRoundBg.getColor());
        }
    }

    public void setRoundBackgroundColor(int color) {
        setRoundBackgroundColor(color, false);
    }

    public void setRoundBackgroundColor(int color, boolean showPressBgColor) {
        if (mRoundBg != null) {
            mRoundBg.setColor(color);
            if (!this.showPressBgColor) {
                this.showPressBgColor = showPressBgColor;
            }
            setDefaultPressColor(mRoundBg.getColor());
        } else {
            setRoundBackground(new ColorDrawable(color));
        }
    }

    public void addStateBgColor(int[] stateSet, int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(stateSet, color);
        }
    }

    public void setPressedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_pressed, color);
        }
    }

    public void setCheckedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_checked, color);
        }
    }

    public void setSelectedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_selected, color);
        }
    }

    public void apply() {
        if (mRoundBg != null) {
            mRoundBg.applyTo(this);
        }
    }
}
