package com.moa.rxdemo.mvp.view.my;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moa.baselib.base.ui.adapter.HolderAdapter;
import com.moa.baselib.base.ui.adapter.ViewHolder;
import com.moa.baselib.utils.AppUtils;
import com.moa.baselib.view.cardview.LinearCardView;
import com.moa.rxdemo.R;

/**
 * FunctionsAdapter
 *
 * @author wangjian
 * Created on 2020/9/27 11:36
 */
public class MyFunctionsAdapter extends HolderAdapter<FunctionItem> {

    public MyFunctionsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected ViewHolder<FunctionItem> createHolder(int position, FunctionItem obj) {
        return new ViewHolder<FunctionItem>() {

            private LinearCardView itemView;
            private TextView tvTitle;
            private ImageView ivImage;

            @Override
            public int getLayoutId() {
                return R.layout.my_function_item;
            }

            @Override
            public void initView(View itemView, FunctionItem data, Context context) {
                this.itemView = (LinearCardView) itemView;
                tvTitle = findViewById(R.id.iv_title);
                ivImage = findViewById(R.id.iv_image);
            }

            @Override
            public void bindData(FunctionItem data, int position, Context context) {
                this.itemView.setCardBackgroundColor(AppUtils.getColor(context, data.bgRes));
                tvTitle.setText(data.titleStringRes);
                ivImage.setImageResource(data.logoRes);
            }
        };
    }
}
