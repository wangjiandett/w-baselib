package com.moa.rxdemo.mvp.view.base.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.moa.baselib.view.recycler.IData
import com.moa.baselib.view.recycler.RecyclerHolder
import com.moa.baselib.view.recycler.RefreshRecyclerAdapter
import com.moa.rxdemo.R
import com.moa.rxdemo.mvp.view.demons.recycler.EmptyHolder
import com.moa.rxdemo.mvp.view.demons.recycler.MyFooterHolder
import com.moa.rxdemo.mvp.view.demons.recycler.MyHeaderHolder

/**
 * 封装刷新和加载的adapter
 *
 * @param recyclerView
 * @param fragment 用于接收empty view点击的reload回调
 */
abstract class RefreshAndLoadAdapter<D : IData>(recyclerView: androidx.recyclerview.widget.RecyclerView, var fragment: RefreshAndLoadFragment2<D>?) : RefreshRecyclerAdapter<D>(recyclerView) {

    override fun onReload() {
        super.onReload()
        fragment?.refresh()
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
        return object : MyFooterHolder(inflateView(viewGroup, R.layout.tt_item_recycler_footer)) {
            override fun initSelector() {
                // 覆盖背景点击颜色
            }
        }
    }

}