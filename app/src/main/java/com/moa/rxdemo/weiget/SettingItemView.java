package com.moa.rxdemo.weiget;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moa.rxdemo.R;

/**
 * 设置界面通用item
 *
 * @author wangjian
 * Created on 2020/8/2 10:47
 */
public class SettingItemView extends RelativeLayout {

    public SettingItemView(Context context) {
        super(context);
        init();
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ImageView mIvStartIcon;
    private TextView mTvText;
    private TextView mTvSubText;
    private ImageView mIvEndIcon;

    private void init() {
        View.inflate(getContext(), R.layout.tt_item_setting, this);
        mIvStartIcon = (ImageView) findViewById(R.id.iv_start_icon);
        mTvText = (TextView) findViewById(R.id.tv_name);
        mTvSubText = (TextView) findViewById(R.id.tv_sub_text);
        mIvEndIcon = (ImageView) findViewById(R.id.iv_end_icon);
    }

    public ImageView getStartIv() {
        return mIvStartIcon;
    }

    public void setStartIcon(int resourceId) {
        getStartIv().setVisibility(VISIBLE);
        getStartIv().setImageResource(resourceId);
    }

    public TextView getTitleTv() {
        return mTvText;
    }

    public TextView getSubTitleTv() {
        return mTvSubText;
    }

    public ImageView getEndIv() {
        return mIvEndIcon;
    }
}
