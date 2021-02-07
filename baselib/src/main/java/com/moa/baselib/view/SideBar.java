package com.moa.baselib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.moa.baselib.R;

/**
 * 带字母的滑动view
 *
 * @author wangjian
 */
public class SideBar extends View {
    private SideBar.OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static final String[] b = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView mTextDialog;

    /**
     * 滑动sidebar时显示当前选中字母的view
     *
     * @param mTextDialog view
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = this.getHeight();
        int width = this.getWidth();
        int singleHeight = height / b.length;

        for (int i = 0; i < b.length; ++i) {
            this.paint.setColor(getResources().getColor(R.color.black));
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setAntiAlias(true);
            this.paint.setTextSize(30.0F);
            if (i == this.choose) {
                this.paint.setColor(getResources().getColor(R.color.white));
                this.paint.setFakeBoldText(true);
            }

            float xPos = (float) (width / 2) - this.paint.measureText(b[i]) / 2.0F;
            float yPos = (float) (singleHeight * i + singleHeight);
            canvas.drawText(b[i], xPos, yPos, this.paint);
            this.paint.reset();
        }

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = this.choose;
        SideBar.OnTouchingLetterChangedListener listener = this.onTouchingLetterChangedListener;
        int c = (int) (y / (float) this.getHeight() * (float) b.length);
        switch (action) {
            case 1:
                this.setBackground(new ColorDrawable(0));
                this.choose = -1;
                this.invalidate();
                if (this.mTextDialog != null) {
                    this.mTextDialog.setVisibility(INVISIBLE);
                }
                break;
            default:
                this.setBackgroundResource(R.drawable.tt_sidebar_bg);
                if (oldChoose != c && c >= 0 && c < b.length) {
                    if (listener != null) {
                        listener.onTouchingLetterChanged(b[c]);
                    }

                    if (this.mTextDialog != null) {
                        this.mTextDialog.setText(b[c]);
                        this.mTextDialog.setVisibility(VISIBLE);
                    }

                    this.choose = c;
                    this.invalidate();
                }
        }

        return true;
    }

    /**
     * 设置滑动监听器
     *
     * @param onTouchingLetterChangedListener 监听器
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String var1);
    }
}
