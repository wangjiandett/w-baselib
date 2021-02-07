package com.moa.rxdemo.mvp.view.demons.recycler

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.moa.baselib.view.recycler.EmptyData
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.baselib.view.recycler.RefreshRecyclerAdapter
import com.moa.baselib.view.recycler.Status
import com.moa.baselib.view.round.RoundButton
import com.moa.rxdemo.R

/**
 * 自定义empty view
 *
 * Created by：wangjian on 2019/1/9 11:33
 */
class EmptyHolder(view: View) : RecyclerHolder<EmptyData>(view) {

    lateinit var mIvTopImg: ImageView
    lateinit var mTvLoadingStatus: TextView
    lateinit var mTvEmptyTip: TextView
    lateinit var mBtnAction: RoundButton
    lateinit var mContainer: LinearLayout

    override fun initView() {
        mIvTopImg = findViewById<ImageView>(R.id.iv_top_img)
        mTvLoadingStatus = findViewById<TextView>(R.id.tv_loading_status)
        mTvEmptyTip = findViewById<TextView>(R.id.tv_empty_tip)
        mBtnAction = findViewById<RoundButton>(R.id.btn_action)
        mContainer = findViewById<LinearLayout>(R.id.container)
    }

    override fun initSelector() {
        // super.initSelector()
    }

    /**
     *empty view显示状态
     *
     * @see Status.SUCCESS
     * @see Status.FAIL
     * @see Status.FINISH
     * @see Status.LOADING
     *
     * @param data 加载状态
     */
    override fun bindData(data: EmptyData) {
        mIvTopImg.setImageResource(R.mipmap.no_report_data)
        when (data.code) {
            Status.LOADING -> {
                mTvLoadingStatus.setText(R.string.loading)
            }
            Status.FAIL -> {
                mTvLoadingStatus.setText(R.string.loading_fail)
                mIvTopImg.setImageResource(R.mipmap.network_error)
            }
            Status.FINISH -> {
                mTvLoadingStatus.setText(R.string.no_more_data)
            }
            -1 -> {
                mTvLoadingStatus.text = data.msg
                mIvTopImg.setImageResource(R.mipmap.no_login_icon)
            }
            1002 -> {
                mTvLoadingStatus.text = data.msg
                mIvTopImg.setImageResource(R.mipmap.network_error)
            }
            else -> {
                val text = if (data.msg != null) data.msg else getString(R.string.click_2_load)
                mTvLoadingStatus.text = text
            }
        }

        itemView.setOnClickListener {
            // 防止正在刷新时点击empty view触发刷新
            if (adapter is RefreshRecyclerAdapter) {
                if ((adapter as RefreshRecyclerAdapter).isRefreshing) {
                    return@setOnClickListener
                }
            }

            if (adapter != null) {
                adapter.onReload()
            }
        }
    }
}