package com.moa.rxdemo.mvp.view.demons.recycler

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.baselib.view.recycler.Status
import com.moa.rxdemo.R

/**
 * 类或文件描述
 *
 * Created by：wangjian on 2019/1/9 11:33
 */
open class MyFooterHolder(view: View) : RecyclerHolder<Int>(view) {

    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    override fun initView() {
        textView = this.findViewById(R.id.tv_name)
        progressBar = this.findViewById(R.id.progressBar)
    }

    override fun bindData(data: Int) {
        progressBar.visibility = View.GONE
        if (data == Status.LOADING) {
            progressBar.visibility = View.VISIBLE
            textView.text = "加载中..."
        } else if (data == Status.FAIL) {
            textView.text = "加载失败，点击重试"
        } else if (data == Status.SUCCESS) {
            textView.text = "点击加载更多数据"
        } else if (data == Status.FINISH) {
            textView.text = "已加载完"
        }
    }
}