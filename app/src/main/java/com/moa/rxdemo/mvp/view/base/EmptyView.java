package com.moa.rxdemo.mvp.view.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moa.baselib.view.round.RoundButton;
import com.moa.rxdemo.R;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2021/1/12 10:45
 */
public class EmptyView extends LinearLayout {

    public ImageView mIvTopImg;
    public TextView mTvLoadingStatus;
    public TextView mTvEmptyTip;
    public RoundButton mBtnAction;
    public LinearLayout mContainer;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.tt_item_empty_list, this);
        mIvTopImg = findViewById(R.id.iv_top_img);
        mTvLoadingStatus = findViewById(R.id.tv_loading_status);
        mTvEmptyTip = findViewById(R.id.tv_empty_tip);
        mBtnAction = findViewById(R.id.btn_action);
        mContainer = findViewById(R.id.container);
    }

    public void setTopImg(int imgRes) {
        mIvTopImg.setImageResource(imgRes);
    }

    public void setLoadingStatus(int loadingStatusRes) {
        mTvLoadingStatus.setText(loadingStatusRes);
    }

    public void setLoadingStatus(String loadingStatusTxt) {
        mTvLoadingStatus.setText(loadingStatusTxt);
    }

    public void setEmptyTip(int emptyTipStatusRes) {
        mTvEmptyTip.setText(emptyTipStatusRes);
    }

    public void setEmptyTip(String emptyTipStatusTxt) {
        mTvEmptyTip.setText(emptyTipStatusTxt);
    }

}
