package com.moa.rxdemo.mvp.view.demons.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.moa.baselib.view.recycler.RefreshRecyclerAdapter
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.rxdemo.R

class MyRefreshRecyclerAdapter(recyclerView: androidx.recyclerview.widget.RecyclerView, var fragment: RefreshRecyclerFragment) : RefreshRecyclerAdapter<Data>(recyclerView) {

    override fun onReload() {
        super.onReload()
        fragment.refresh()
    }

    override fun getEmptyViewHolder(viewGroup: ViewGroup?): RecyclerHolder<*> {
        val holder = EmptyHolder(inflateView(viewGroup, R.layout.tt_item_empty_list))
        holder.adapter = this
        return holder
    }

    override fun getHeaderViewHolder(viewGroup: ViewGroup?): RecyclerHolder<*> {
        return MyHeaderHolder(inflateView(viewGroup, R.layout.tt_item_recycler_header))
    }

    override fun getFooterViewHolder(viewGroup: ViewGroup?): RecyclerHolder<*> {
        return MyFooterHolder(inflateView(viewGroup, R.layout.tt_item_recycler_footer))
    }

    override fun getViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerHolder<Data> {
        return MyRecyclerHolder(inflateView(viewGroup, R.layout.tt_item_recycler))
    }
}