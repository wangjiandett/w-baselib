package com.moa.rxdemo.mvp.view.guide;

import java.io.Serializable;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2021/1/23 11:09
 */
public class GuideItem implements Serializable {
    public int guideImgRes;
    public int guideTextRes;
    public int position;

    public GuideItem(int guideImgRes, int guideTextRes, int position) {
        this.guideImgRes = guideImgRes;
        this.guideTextRes = guideTextRes;
        this.position = position;
    }

    public GuideItem(int guideImgRes, int guideTextRes) {
        this.guideImgRes = guideImgRes;
        this.guideTextRes = guideTextRes;
    }
}
