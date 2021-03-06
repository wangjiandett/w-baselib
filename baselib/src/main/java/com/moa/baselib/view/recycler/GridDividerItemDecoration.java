package com.moa.baselib.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.moa.baselib.utils.DisplayUtils;

/**
 * RecyclerView网格分割线
 *
 * @author wangjian
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int mDividerHeight;
    private boolean enableEmptyView = false;
    private boolean hasHeader = false;
    private boolean hasFooter = false;

    public GridDividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 设置分割线drawable
     *
     * @param drawable drawable
     */
    public void setDrawable(@NonNull Drawable drawable) {
        this.mDivider = drawable;
        this.mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 设置分割线drawable
     *
     * @param color 分割线颜色
     * @param height 分割线高度，单位dp
     */
    public void setDrawable(int color, int height) {
        this.mDivider = new ColorDrawable(color);
        this.mDividerHeight = DisplayUtils.dip2px(height);
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    /**
     * 特殊处理当只显示empty view的时候分割线的显示
     *
     * @param enableEmptyView
     */
    public void setEnableEmptyView(boolean enableEmptyView) {
        this.enableEmptyView = enableEmptyView;
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDividerHeight;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHeight;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getOrientation(RecyclerView.LayoutManager layoutManager) {
        int orientation = LinearLayoutManager.VERTICAL;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
        }

        return orientation;

    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = getOrientation(layoutManager);
            if (orientation == GridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            }

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = getOrientation(layoutManager);
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            int orientation = getOrientation(layoutManager);
            if (orientation == GridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            } else {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemAdapterPosition = parent.getChildAdapterPosition(view);
        int itemPosition = parent.getChildLayoutPosition(view);

        // 有header的时候
        if (hasHeader) {
            itemPosition = itemPosition - 1;
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int orientation = getOrientation(layoutManager);
        // 竖直和水平  最后一列和最后一行是颠倒的
        if (orientation == LinearLayoutManager.VERTICAL) {
            // 如果是最后一行，则不需要绘制底部
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, 0, mDividerHeight, 0);
            }
            // 如果是最后一列，则不需要绘制右边
            else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, 0, 0, mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerHeight, mDividerHeight);
            }
        } else {
            // 如果是最后一行，则不需要绘制底部
            if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, 0, 0, mDividerHeight);
            }
            // 如果是最后一列，则不需要绘制右边
            else if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                outRect.set(0, 0, mDividerHeight, 0);
            } else {
                outRect.set(0, 0, mDividerHeight, mDividerHeight);
            }
        }

        // 处理显示empty的情况
        if(enableEmptyView){
            if((hasHeader && childCount == 2 && itemAdapterPosition == 1)// header 和 empty 同时显示
               || !hasHeader && childCount == 1 && itemAdapterPosition == 0){// 只显示empty
                outRect.set(0, 0, 0, 0);
            }
        }

        // 可能被上面的覆盖，所以放到最后
        // 是否有footer
        boolean drawFooter = (hasFooter && itemAdapterPosition == childCount - 1);

        // footer不用绘制底部和右边
        if (drawFooter) {
            outRect.set(0, 0, 0, 0);
        }
    }
}