package com.moa.rxdemo.mvp.view.demons.recycler

import android.view.ViewGroup
import com.moa.baselib.view.recycler.RecyclerAdapter
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.rxdemo.R

/**
 * 类或文件描述
 *
 * Created by：wangjian on 2019/1/9 11:46
 */
class ManagerAdapter: RecyclerAdapter<Data>() {

    override fun getEmptyViewHolder(viewGroup: ViewGroup?): RecyclerHolder<*> {
        return EmptyHolder(inflateView(viewGroup, R.layout.tt_item_empty_list))
    }

    override fun getViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerHolder<Data> {
        return MyRecyclerHolder(inflateView(viewGroup, R.layout.tt_item_recycler))
    }
}