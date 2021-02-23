package com.moa.rxdemo.mvp.view.my;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.rxdemo.R;
import com.moa.rxdemo.utils.AppConfig;
import com.moa.rxdemo.utils.GradingUtils;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2021/2/21 15:32
 */
public class DebugFragment extends BaseFragment {

    EditText inputEt;
    Button saveBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_debug;
    }

    @Override
    protected void initView(@NonNull View view) {
        inputEt = findViewById(R.id.et_input);
        saveBtn = findViewById(R.id.btn_save);

        inputEt.setText(AppConfig.getLocalBaseUrl());

        saveBtn.setOnClickListener(v -> {
            GradingUtils.logout();
            String text = inputEt.getText().toString();
            AppConfig.saveLocalBaseUrl(text);


            showToast(R.string.modify_base_url_tip);

            // System.exit(0);
            // android.os.Process.killProcess(android.os.Process.myPid());
        });
    }
}
