package com.moa.baselib.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.moa.baselib.utils.DateFormatting;

/**
 * 自定义倒计时器，支持自定义格式化样式
 *
 * @author wangjian
 * Created on 2020/8/2 16:26
 */
public class CountDownTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 倒计时定时器
    private CountDownTimer timer;
    // 默认显示的text
    private String mTipText;
    // 格式化最终显示的字符串
    private int mFormatResId;
    // 倒计时总时间
    private int mSecondsInFuture;
    // 倒计时每次递减时间
    private int mCountDownInterval;
    // 是否一致显示0作为占位符
    private boolean mAlways2Num = false;
    // 是否将时间格式化成HH:mm:ss样式
    private boolean mNeedFormat = true;
    // 是否正在倒计时
    private boolean isTicking;

    /**
     * 开始倒计时，如10秒，每次递减1秒，或 5:00:00小时，每次递减1秒
     *
     * @param secondsInFuture   倒计时总时间, 单位秒
     * @param countDownInterval 每次减少时间，单位秒
     */
    public void start(int secondsInFuture, int countDownInterval) {
        this.mSecondsInFuture = secondsInFuture;
        this.mCountDownInterval = countDownInterval;

        // 如：倒计时5000毫秒
        // 每过1000毫秒执行一次onTick
        // 倒计时完成执行onFinish
        timer = new CountDownTimer(secondsInFuture * 1000, countDownInterval * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                onTicking(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                onTickFinish();
            }
        };

        timer.start();
        isTicking = true;
        setEnabled(false);
    }

    /**
     * 结束倒计时，在activity和fragment中onDestroy时需要调用，防止造成内存泄露
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 设置默认显示text，必须设置，否则view不显示
     *
     * @param preText 默认显示text
     */
    public void setTipText(String preText) {
        mTipText = preText;
    }

    /**
     * 格式化时间，当第一位为0时，是否省略0，如：09:08:01,9:8:1，默认false
     *
     * @param mAlways2Num 是否显示0作为占位符，如：9:8:1 -> 09:08:01
     */
    public void setAlways2Num(boolean mAlways2Num) {
        this.mAlways2Num = mAlways2Num;
    }

    /**
     * 拼接最终显示的字符串样式，默认显示HH:mm:ss，或小于1分钟显示秒（s）
     *
     * @param mFormatResId 资源id
     */
    public void setShowFormatResId(int mFormatResId) {
        this.mFormatResId = mFormatResId;
    }

    /**
     * 是否将时间格式化成HH:mm:ss格式，小于1分钟显示秒，默认为true
     *
     * @param mNeedFormat true 格式化成HH:mm:ss格式，小于1分钟显示秒
     *                    false 只显示秒
     */
    public void setTimeNeedFormat(boolean mNeedFormat) {
        this.mNeedFormat = mNeedFormat;
    }

    /**
     * 设置当前倒计时是否可点击
     *
     * @param clickEnable 是否可点击
     */
    public void setClickEnable(boolean clickEnable){
        if(isTicking){
            return;
        }
        setEnabled(clickEnable);
    }

    /**
     * 倒计时结束时调用
     */
    private void onTickFinish() {
        isTicking = false;
        setEnabled(true);
        if (!TextUtils.isEmpty(mTipText)) {
            setText(mTipText);
        } else {
            String value = DateFormatting.getTimeString(mSecondsInFuture / 1000, mAlways2Num);
            setText(value);
        }
    }

    /**
     * 每次倒计时时调用
     *
     * @param millisUntilFinished 剩余结束时间
     */
    private void onTicking(long millisUntilFinished) {
        isTicking = true;
        int time = (int) (millisUntilFinished / 1000);
        String tickText = String.valueOf(time);
        // 格式化时间成HH:mm:ss格式
        if (mNeedFormat) {
            tickText = DateFormatting.getTimeString(time, mAlways2Num);
        }

        // 拼接最终显示的字符串
        if (mFormatResId > 0) {
            setText(getContext().getResources().getString(mFormatResId, tickText));
        } else {
            setText(tickText);
        }
    }
}
