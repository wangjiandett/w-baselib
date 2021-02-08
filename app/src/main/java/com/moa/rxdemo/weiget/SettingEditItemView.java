package com.moa.rxdemo.weiget;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moa.baselib.utils.SimpleTextWatcher;
import com.moa.rxdemo.R;

/**
 * 设置界面带edit通用item
 *
 * @author wangjian
 * Created on 2020/8/2 10:47
 */
public class SettingEditItemView extends RelativeLayout {

    public SettingEditItemView(Context context) {
        super(context);
        init();
    }

    public SettingEditItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingEditItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingEditItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ImageView mIvStartIcon;
    private TextView mTvTitle;
    private EditText mEtText;
    private ImageView mIvEndIcon;

    private TextChangeListener mTextChangeListener;
    private OnClickListener mOnEndIconClickListener;

    private void init() {
        View.inflate(getContext(), R.layout.tt_item_setting_edit, this);
        mIvStartIcon = (ImageView) findViewById(R.id.iv_start_icon);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mEtText = (EditText) findViewById(R.id.tv_name);
        mIvEndIcon = (ImageView) findViewById(R.id.iv_end_icon);

        mEtText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mIvEndIcon.setVisibility(!TextUtils.isEmpty(s) ? VISIBLE : GONE);
                if (mTextChangeListener != null) {
                    mTextChangeListener.afterTextChanged(s);
                }
            }
        });

        // 末尾的mIvEndIcon点击事件
        mIvEndIcon.setOnClickListener(v -> {
            if (mOnEndIconClickListener != null) {
                mOnEndIconClickListener.onClick(v);
            } else {
                mEtText.setText("");
            }
        });
    }

    public ImageView getStartIv() {
        mIvStartIcon.setVisibility(VISIBLE);
        return mIvStartIcon;
    }

    public void setStartIcon(int resourceId) {
        getStartIv().setImageResource(resourceId);
    }

    public TextView getTitleTv() {
        mTvTitle.setVisibility(VISIBLE);
        return mTvTitle;
    }

    public EditText getInputEt() {
        return mEtText;
    }

    /**
     * 设置输入类型为密码
     */
    public void setInputTypePwd() {
        mEtText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    /**
     * 当调用setInputTypePwd的时候，切换密码是否可见
     */
    public void togglePwdVisible() {
        TransformationMethod method = getInputEt().getTransformationMethod();
        if(method instanceof HideReturnsTransformationMethod){
            getInputEt().setTransformationMethod(PasswordTransformationMethod.getInstance());//密码不可见
        }else{
            getInputEt().setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //密码可见
        }
    }

    /**
     * 设置输入类型为手机号
     */
    public void setInputTypePhone() {
        mEtText.setInputType(InputType.TYPE_CLASS_PHONE);
        mEtText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
    }

    public ImageView getEndIv() {
        return mIvEndIcon;
    }

    public void setTextChangeListener(TextChangeListener mTextChangeListener) {
        this.mTextChangeListener = mTextChangeListener;
    }

    public void setOnEndIconClickListener(OnClickListener mOnEndIconClickListener) {
        this.mOnEndIconClickListener = mOnEndIconClickListener;
    }

    public interface TextChangeListener {
        public void afterTextChanged(Editable s);
    }

}
