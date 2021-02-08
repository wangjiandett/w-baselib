package com.moa.rxdemo.mvp.view.base.recycler

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.AppUtils
import com.moa.baselib.view.recycler.EmptyData
import com.moa.baselib.view.recycler.IData
import com.moa.baselib.view.recycler.LinearDividerItemDecoration
import com.moa.baselib.view.recycler.Status
import com.moa.rxdemo.R

/**
 * RecyclerView 支持上拉加载下拉刷新
 *
 * @author wangjian
 * Created on 2020/10/22 15:24
 */
abstract class RefreshAndLoadFragment2<D : IData> : BaseFragment() {

    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    lateinit var mAdapter: RefreshAndLoadAdapter<D>
    var mCurrentPage = 1
    var mPageSize = 10
    var mPageTotal = 1

    override fun initView(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout)
        swipeRefreshLayout.setColorSchemeResources(R.color.tt_colorAccent)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = getLayoutManager()
        recyclerView.addItemDecoration(getDivider())

        mAdapter = getAdapter(recyclerView)
        recyclerView.adapter = mAdapter

        // enable footer
        mAdapter.isFooterViewEnable = true
        // auto load more
        // mAdapter.setAutoLoadMore(false)

        // enable empty view
        mAdapter.isEmptyViewEnable = true

        // load more Listener
        mAdapter.setOnLoadMoreListener {
            loadData()
        }

        // refresh Listener
        mAdapter.setOnRefreshListener(swipeRefreshLayout) {
            refresh()
        }
    }

    abstract fun getAdapter(recyclerView: androidx.recyclerview.widget.RecyclerView): RefreshAndLoadAdapter<D>

    open fun getDivider(): LinearDividerItemDecoration{
        val divider = LinearDividerItemDecoration(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
        divider.setDrawable(AppUtils.getColor(activity, R.color.list_divider), 1)
        return divider
    }

    open fun getLayoutManager(): androidx.recyclerview.widget.RecyclerView.LayoutManager{
        return androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    fun refresh() {
        mCurrentPage = 1
        loadData()
    }

    fun getRequestMap(): HashMap<String, String> {
        val request = hashMapOf<String, String>()
        request["page"] = mCurrentPage.toString()
        request["pageSize"] = mPageSize.toString()
        return request
    }

    open fun loadData() {

    }

    open fun onLoadSuccess(rows: List<D>?) {
        if (rows != null) {
            when (mCurrentPage) {
                1 -> {
                    when {
                        rows.isEmpty() -> {
                            mAdapter.emptyViewStatus = EmptyData.getFinish()
                            mAdapter.onRefreshFinish(rows, true)
                        }
                        rows.size < mPageSize -> {
                            mAdapter.onRefreshFinish(rows, true)
                        }
                        else -> {
                            mAdapter.onRefreshFinish(rows)
                            mCurrentPage++
                        }
                    }
                }
                mPageTotal -> {
                    mAdapter.onLoadFinish(rows, true)
                }
                else -> {
                    mAdapter.onLoadFinish(rows, false)
                }
            }
        }
    }

    open fun onLoadFail(errorCode: Int, msg: String?) {
        showToast(msg)
        if (mCurrentPage == 1) {
            mAdapter.onRefreshFail(EmptyData(errorCode, msg))
        } else {
            mAdapter.onLoadFail()
        }
    }

    open fun onLoadFail(msg: String?) {
        onLoadFail(Status.UNUSED, msg)
    }
}