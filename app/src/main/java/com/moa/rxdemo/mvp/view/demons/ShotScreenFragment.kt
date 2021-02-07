package com.moa.rxdemo.mvp.view.demons

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.ScreenShot
import com.moa.rxdemo.R


/**
 * 截屏功能演示
 *
 * Created by：wangjian on 2018/12/21 15:58
 */
class ShotScreenFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_camera
    }

    override fun initView(view: View) {
        super.initView(view)
        view.let { it ->
            val img = findViewById<ImageView>(R.id.iv_img)
            val view = findViewById<FrameLayout>(R.id.fl_container)
            it.findViewById<View>(R.id.btn_view_shot).setOnClickListener {
                img.setImageBitmap(ScreenShot.takeViewShot(view))
            }

            it.findViewById<View>(R.id.btn_screen_shot).setOnClickListener {
                img.setImageBitmap(ScreenShot.takeScreenShot(activity))
            }
        }
    }

}