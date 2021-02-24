package com.moa.rxdemo.mvp.view.base

import android.text.TextUtils
import android.view.View
import android.widget.ListView
import com.moa.baselib.base.ui.BaseListFragment
import com.moa.baselib.view.round.RoundButton
import com.moa.baselib.view.swipetoloadlayou.OnLoadMoreListener
import com.moa.baselib.view.swipetoloadlayou.OnRefreshListener
import com.moa.baselib.view.swipetoloadlayou.SwipeToLoadLayout
import com.moa.rxdemo.R


/**
 * 支持刷新和加载更多的ListView列表界面
 *
 * @author wangjian
 * Created on 2020/10/7 16:16
 */
abstract class RefreshAndLoadFragment<T> : BaseListFragment<T>(), OnRefreshListener, OnLoadMoreListener {

    lateinit var mEmptyView: EmptyView

    lateinit var swipeLoadLayout: SwipeToLoadLayout
    lateinit var mListView: ListView

    var isLoadMoreEnable = true

    var currentPage = 1
    var pageSize = 10
    var mPageTotal = 1

    override fun initView(view: View) {
        super.initView(view)
        swipeLoadLayout = findViewById(R.id.swipe_layout)
        // 设置上拉下拉监听器
        swipeLoadLayout.setOnRefreshListener(this)
        swipeLoadLayout.setOnLoadMoreListener(this)
        swipeLoadLayout.setAutoLoadMoreEnable(true)

        mListView = findViewById(R.id.tt_list)
        bindAdapter(mListView)
        mEmptyView = findViewById<EmptyView>(R.id.ll_empty)
        mListView.emptyView = mEmptyView
        setOnClickListener(mEmptyView)
    }

    override fun onRefresh() {
        currentPage = 1
        isLoadMoreEnable = true
        loadData()
    }

    override fun onLoadMore() {
        loadData()
    }

    open fun loadData() {
    }

    fun getRequestMap(): HashMap<String, String> {
        val request = hashMapOf<String, String>()
        request["page"] = currentPage.toString()
        request["pageSize"] = pageSize.toString()
        return request
    }

    open fun onLoadSuccess(rows: List<T>?) {
        if (rows != null) {
            if (currentPage == 1) {
                if (rows.isEmpty()) {
                    onDataEmpty()
                }
                mHolderAdapter.setListAndNotify(rows)
            } else {
                mHolderAdapter.addAllAndNotify(rows)
            }
            // 设置更新页码
            if (mPageTotal > currentPage) {
                currentPage++
            } else {
                isLoadMoreEnable = false
            }
        }
        hideProgress()
        finishRefresh()
    }

    open fun onLoadFail(msg: String?) {
        if (isAdded && !isDetached) {
            // 此处由于没有返回错误类型，暂时使用字符串比较代替
            if (TextUtils.equals(getString(com.moa.baselib.R.string.tt_error_connect), msg)) {
                setDataEmptyImg(R.mipmap.network_error)
            } else {
                setDataEmptyImg(R.mipmap.no_report_data)
            }
            setEmptyTipTxt(msg)
            hideProgress()
            showToast(msg)
            finishRefresh()
        }
    }

    /**
     * 结束刷新或加载
     */
    open fun finishRefresh() {
        if (swipeLoadLayout.isRefreshing) {
            swipeLoadLayout.isRefreshing = false
        }

        if (swipeLoadLayout.isLoadingMore) {
            swipeLoadLayout.isLoadingMore = false
        }

        swipeLoadLayout.isLoadMoreEnabled = isLoadMoreEnable
    }

    open fun setDataEmptyImg(imgRes: Int) {
        mEmptyView.setTopImg(imgRes)
    }

    /**
     * 数据为空时显示的提示文本
     */
    open fun onDataEmpty() {
        setEmptyTipTxt(getString(R.string.loading_no_more_data))
        setDataEmptyImg(R.mipmap.no_report_data)
    }

    /**
     * 设置empty view提示文本
     */
    open fun setEmptyTipTxt(tipTxt: String?) {
        mEmptyView.setLoadingStatus(tipTxt)
    }

    fun getActionButton(intRes: Int): RoundButton {
        mEmptyView.mBtnAction.setText(intRes)
        mEmptyView.mBtnAction.visibility = View.VISIBLE
        return mEmptyView.mBtnAction
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == mEmptyView) {
            if (!swipeLoadLayout.isRefreshing || !swipeLoadLayout.isLoadingMore) {
                onEmptyViewClick(mEmptyView)
            }
        }
    }

    /**
     * empty view点击事件
     */
    open fun onEmptyViewClick(emptyView: View) {
        // onRefresh()
        swipeLoadLayout.isRefreshing = true
    }
}