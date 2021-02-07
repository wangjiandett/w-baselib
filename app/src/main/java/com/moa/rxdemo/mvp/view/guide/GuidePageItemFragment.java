package com.moa.rxdemo.mvp.view.guide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.rxdemo.R;

import org.jetbrains.annotations.NotNull;


public class GuidePageItemFragment extends BaseFragment {

    public static final String EXTRA = "extra";
    private TextView mTvSkip;
    private ImageView mIvGuideImg;
    private GuideItem mItem;

    public static GuidePageItemFragment create(GuideItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA, item);
        GuidePageItemFragment fragment = new GuidePageItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_guide_item;
    }

    @Override
    protected void initView(@NotNull @NonNull View view) {
        super.initView(view);

        mTvSkip = (TextView) findViewById(R.id.tv_skip);
        mIvGuideImg = (ImageView) findViewById(R.id.iv_guide_img);

        setOnClickListener(mTvSkip);
    }


    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItem = (GuideItem) bundle.getSerializable(EXTRA);
            if (mItem != null) {
                mIvGuideImg.setImageResource(mItem.guideImgRes);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mTvSkip) {
            if (getActivity() instanceof GuideActivity) {
                ((GuideActivity) getActivity()).guideFinish();
            }
        }
    }
}
