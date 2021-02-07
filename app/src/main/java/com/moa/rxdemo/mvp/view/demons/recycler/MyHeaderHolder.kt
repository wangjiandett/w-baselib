package com.moa.rxdemo.mvp.view.demons.recycler

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.rxdemo.R

/**
 * 类或文件描述
 *
 * Created by：wangjian on 2019/1/9 11:33
 */

class MyHeaderHolder(view: View) : RecyclerHolder<String>(view) {


    private lateinit var textView: TextView
    lateinit var progressBar: ProgressBar

    override fun initView() {
        textView = this.findViewById(R.id.tv_name)
    }

    override fun bindData(data: String?) {
        textView.text = "header:$data"
    }
}