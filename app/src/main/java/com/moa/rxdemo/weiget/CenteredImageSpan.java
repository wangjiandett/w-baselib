package com.moa.rxdemo.weiget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * TextView中显示居中的ImageSpan
 *
 * @author wangjian
 * Created on 2021/1/24 10:49
 * 参考链接：https://www.cnblogs.com/withwind318/p/5541267.html
 */
public class CenteredImageSpan extends ImageSpan {

    public CenteredImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - b.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
