package com.moa.rxdemo.mvp.view.demons

import android.graphics.Color
import android.view.View
import android.widget.Button
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.DialogUtils
import com.moa.baselib.utils.DisplayUtils
import com.moa.baselib.utils.LogUtils
import com.moa.baselib.view.CircleTextProgressbar
import com.moa.baselib.view.CountDownTextView
import com.moa.baselib.view.SwitchButton
import com.moa.rxdemo.R

/**
 * dispatcher 使用demo
 *
 * Created by：wangjian on 2018/12/21 15:58
 */
class ViewsFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_views;
    }

    var downView: CountDownTextView? = null;

    override fun initView(view: View) {
        super.initView(view)
        view.let {

            // 测试没有回调的 switchButton
            val switchButton = view.findViewById(R.id.btn_switch) as SwitchButton
            switchButton.setOnCheckedChangeListener { btn, isCheck ->
                LogUtils.d("isCheck：$isCheck");
            }
            view.findViewById<View>(R.id.btn_toogle).setOnClickListener {
                switchButton.toggleNoEvent()
            }

            // 对话框
            view.findViewById<View>(R.id.btn_show_dialog).setOnClickListener {
                DialogUtils.showCenterDialog(context, R.layout.tt_dialog_view, true)
            }

            downView = view.findViewById<CountDownTextView>(R.id.tv_count_down)
            downView?.setOnClickListener {
                downView?.setAlways2Num(true)
                downView?.start(5 * 60 * 60, 1)
            }

            // 圆形进度条
            progressBar(view)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        downView?.stop()
    }

    private fun progressBar(view: View) {
        val progressbar = view.findViewById(R.id.progressbar) as CircleTextProgressbar
        progressbar.progress = 50

        progressbar.progressType = CircleTextProgressbar.ProgressType.COUNT
        progressbar.setOutLineColor(Color.RED)
        progressbar.setOutLineWidth(DisplayUtils.dip2px(10f))

        progressbar.setInCircleColor(Color.YELLOW)

        progressbar.setProgressColor(Color.GREEN)
        progressbar.setProgressLineWidth(DisplayUtils.dip2px(5f))

        progressbar.timeMillis = 3000

        progressbar.setInSpace2Out(DisplayUtils.dip2px(5f))
        progressbar.setStartAngle(90f)


        progressbar.setCountdownProgressListener(1) { what, progress ->
            LogUtils.d("progress: $progress")
            progressbar.text = "$progress%"
        };

        val startBtn = view.findViewById(R.id.btn_start_progress) as Button
        startBtn.setOnClickListener {
            progressbar.reStart()
        }
    }

}