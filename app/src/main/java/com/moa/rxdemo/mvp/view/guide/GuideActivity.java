package com.moa.rxdemo.mvp.view.guide;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.moa.baselib.base.ui.BaseActivity;
import com.moa.baselib.base.ui.adapter.PagesAdapter;
import com.moa.baselib.utils.DisplayUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.Router;
import com.moa.rxdemo.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序引导页
 * <p>
 * Created by：wangjian on 2017/12/20 16:25
 */
public class GuideActivity extends BaseActivity {

    private ViewPager mViewPager;
    private RadioGroup radioGroup;
    private View enterView;
    private MyAdapter mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.tt_activity_guide;
    }

    @Override
    protected void initView() {
        super.initView();
        // 保存引导状态
        AppConfig.saveNeedShowGuide(false);

        mViewPager = findViewById(R.id.view_pager);
        radioGroup = findViewById(R.id.radio_group);
        enterView = findViewById(R.id.tv_enter);

        mPagerAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                radioGroupCheckIndex(position);
                if (position == mPagerAdapter.getCount() - 1) {
                    //radioGroup.setVisibility(View.GONE);
                    enterView.setVisibility(View.VISIBLE);
                } else {
                    //radioGroup.setVisibility(View.VISIBLE);
                    enterView.setVisibility(View.GONE);
                }
            }
        });



        enterView.setOnClickListener(v -> guideFinish());

        List<GuideItem> dataList = new ArrayList<>();
        dataList.add(new GuideItem(R.mipmap.guide_page_1, R.mipmap.guide_page_1, 0));
        dataList.add(new GuideItem(R.mipmap.guide_page_2, R.mipmap.guide_page_2, 1));
        dataList.add(new GuideItem(R.mipmap.guide_page_3, R.mipmap.guide_page_3, 2));
        dataList.add(new GuideItem(R.mipmap.guide_page_4, R.mipmap.guide_page_4, 3));

        // 封装显示界面
        List<Fragment> itemFragments = new ArrayList<>();
        for (GuideItem item : dataList) {
            itemFragments.add(GuidePageItemFragment.create(item));
        }
        mPagerAdapter.setFragments(itemFragments);
        mPagerAdapter.notifyDataSetChanged();

        // 初始化indicator
        //initIndicators();
    }

    /**
     * 初始化indicator
     */
    private void initIndicators() {
        radioGroup.removeAllViews();
        // 需要去除第一个和最后一个重复的item
        int count = mPagerAdapter.getCount();
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(DisplayUtils.dip2px(15), DisplayUtils.dip2px(15));
        params.leftMargin = DisplayUtils.dip2px(3);
        params.rightMargin = DisplayUtils.dip2px(3);

        for (int i = 0; i < count; i++) {
            View view = View.inflate(this, R.layout.splash_radio_item, null);
            radioGroup.addView(view, params);
        }

        // 默认选中第一个
        radioGroupCheckIndex(0);
    }

    /**
     * 根据index设置item选中状态
     *
     * @param index 当前选择的item的索引
     */
    private void radioGroupCheckIndex(int index) {
        if (index < radioGroup.getChildCount()) {
            View view = radioGroup.getChildAt(index);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                radioButton.setChecked(true);
            }
        }
    }

    private static class MyAdapter extends PagesAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    /**
     * 引导结束
     */
    public void guideFinish(){
        Router.goMain(this);
        finish();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, GuideActivity.class);
    }
}
