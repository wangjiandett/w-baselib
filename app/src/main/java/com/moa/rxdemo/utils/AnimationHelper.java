package com.moa.rxdemo.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 简单动画帮助类
 *
 * @author wangjian
 * Created on 2020/10/7 9:39
 */
public class AnimationHelper {

    /**
     * 向右移出隐藏view动画
     *
     * @param hideView 需要隐藏的view
     * @param showView 动画结束需要显示的view，可为空
     */
    public static void translateToRightAndHide(View hideView, View showView){
        Animation animation = AnimationUtils.makeOutAnimation(hideView.getContext(), true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(showView != null){
                    showView.setVisibility(View.VISIBLE);
                }
                hideView.setVisibility(View.GONE);
                hideView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hideView.startAnimation(animation);
    }

    /**
     * 向左移动显示view动画
     *
     * @param showView 需要显示的view
     * @param hideView 移动开始时需要隐藏的view，可为空
     */
    public static void translateToLeftAndShow(View showView, View hideView){
        Animation animation = AnimationUtils.makeInAnimation(showView.getContext(), false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(hideView != null){
                    hideView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showView.setVisibility(View.VISIBLE);
        showView.startAnimation(animation);
    }

}
