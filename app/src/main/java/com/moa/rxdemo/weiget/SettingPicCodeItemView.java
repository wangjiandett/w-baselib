package com.moa.rxdemo.weiget;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moa.rxdemo.R;

/**
 * 设置界面带edit通用item
 *
 * @author wangjian
 * Created on 2020/8/2 10:47
 */
public class SettingPicCodeItemView extends RelativeLayout {

    public SettingPicCodeItemView(Context context) {
        super(context);
        init();
    }

    public SettingPicCodeItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingPicCodeItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingPicCodeItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ImageView mIvStartIcon;
    private TextView mTvTitle;
    private EditText mEtText;
    private ImageView mIvEndIcon;
    private ImageView mIvCodeIcon;

    private TextChangeListener mTextChangeListener;

    private void init() {
        View.inflate(getContext(), R.layout.tt_item_setting_pic_code, this);
        mIvStartIcon = (ImageView) findViewById(R.id.iv_start_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mEtText = (EditText) findViewById(R.id.tv_name);
        mIvEndIcon = (ImageView) findViewById(R.id.iv_end_icon);
        mIvCodeIcon = (ImageView) findViewById(R.id.iv_code_icon);


        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mIvEndIcon.setVisibility(!TextUtils.isEmpty(s) ? VISIBLE : GONE);
                if (mTextChangeListener != null) {
                    mTextChangeListener.afterTextChanged(s);
                }
            }
        });

        mIvEndIcon.setOnClickListener(v -> {
            mEtText.setText("");
        });
    }

    public ImageView getStartIv() {
        return mIvStartIcon;
    }

    public void setStartIcon(int resourceId) {
        getStartIv().setVisibility(VISIBLE);
        getStartIv().setImageResource(resourceId);
    }

    public TextView getTitleTv() {
        return mTvTitle;
    }

    public EditText getInputEt() {
        return mEtText;
    }

    public ImageView getEndIv() {
        return mIvEndIcon;
    }

    public ImageView getIvCodeIcon() {
        return mIvCodeIcon;
    }

    public void setTextChangeListener(TextChangeListener mTextChangeListener) {
        this.mTextChangeListener = mTextChangeListener;
    }

    public interface TextChangeListener {
        public void afterTextChanged(Editable s);
    }

}
