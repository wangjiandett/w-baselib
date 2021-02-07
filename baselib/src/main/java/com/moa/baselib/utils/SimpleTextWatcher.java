package com.moa.baselib.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 覆盖TextWatcher中的方法，可选择性实现
 *
 * @author wangjian
 * Created on 2020/11/4 16:39
 */
public abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
