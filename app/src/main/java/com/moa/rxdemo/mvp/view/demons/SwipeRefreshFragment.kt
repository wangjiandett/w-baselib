package com.moa.rxdemo.mvp.view.demons

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.moa.baselib.base.ui.BaseListFragment
import com.moa.baselib.base.ui.adapter.ViewHolder
import com.moa.baselib.utils.FileUtil
import com.moa.baselib.utils.Files
import com.moa.baselib.utils.LogUtils
import com.moa.baselib.utils.ShareUtils
import com.moa.baselib.view.swipetoloadlayou.OnLoadMoreListener
import com.moa.baselib.view.swipetoloadlayou.OnRefreshListener
import com.moa.baselib.view.swipetoloadlayou.SwipeToLoadLayout
import com.moa.rxdemo.R
import com.moa.rxdemo.mvp.bean.ForecastBean
import com.moa.rxdemo.mvp.contract.SwipeContract
import com.moa.rxdemo.mvp.model.SwipeModel
import com.moa.rxdemo.mvp.presenter.SwipePresenter
import com.moa.rxdemo.utils.ImageUtils
import java.io.File

/**
 * SwipeRefresh view 使用demo
 *
 * Created by：wangjian on 2018/12/22 13:14
 */
class SwipeRefreshFragment : BaseListFragment<ForecastBean.DataBean>(), SwipeContract.ISwipeView, OnRefreshListener, OnLoadMoreListener {

    private lateinit var swipeLoadLayout: SwipeToLoadLayout
    private lateinit var gridView: ListView

    private lateinit var presenter: SwipePresenter

    // 分页数据条数
    private val pageSize = 20

    // 当前第几页
    private var currentPage: Int = 1

    // 数据是否加载完成
    private var isDataLoadFinish: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_swipe_refresh
    }

    override fun initView(view: View) {
        super.initView(view)
        presenter = SwipePresenter(this, SwipeModel())

        view.let { it ->
            swipeLoadLayout = it.findViewById(R.id.swipe_layout)
            // 设置上拉下拉监听器
            swipeLoadLayout.setOnRefreshListener(this)
            swipeLoadLayout.setOnLoadMoreListener(this)

            gridView = it.findViewById(R.id.tt_swipe_target)
            gridView.setOnItemClickListener { parent, view, position, id ->
                val item = adapter.getItem(position)
                ImageUtils.loadAsFile(activity, item.imgUrl) { file ->
                    LogUtils.d(file.absolutePath)
                    showToast(file.absolutePath)
                    val exFile = File(Files.getExternalStorageFileDir(), file.name)
                    // copy到外存，并保存到图库，并分享
                    FileUtil.copy(file, exFile)
                    FileUtil.saveImageToGallery(activity, exFile)
                    ShareUtils.shareFile2QQ(activity, file)
                }
            }
            bindAdapter(gridView)
        }
    }

    override fun initData() {
        super.initData()
        loadSwipeList()
    }

    private fun loadSwipeList() {
        presenter.getSwipeList(101220101)
    }

    /**
     * 刷新操作
     */
    override fun onRefresh() {
        currentPage = 1
        loadSwipeList()
    }

    /**
     * 上拉加载操作
     */
    override fun onLoadMore() {
        if (isDataLoadFinish) {
            swipeLoadLayout.isLoadMoreEnabled = false
        }
        loadSwipeList()
    }

    override fun onSuccess(itemList: MutableList<ForecastBean.DataBean>?) {
        itemList?.let {
            if (currentPage == 1) {
                swipeLoadLayout.isRefreshing = false
                mHolderAdapter.dataList = itemList
            } else {
                swipeLoadLayout.isLoadingMore = false
                mHolderAdapter.dataList.addAll(itemList)
            }

            // 加载条数等于pageSize时，说明还有数据
            // 此时pageSize+1
            if (itemList.size == pageSize) {
                currentPage += 1

            }
            // 加载的条数小于pageSize说明数据加载完了，就不要上拉加载了
            else if (itemList.size < pageSize) {
                isDataLoadFinish = true
            }

            mHolderAdapter.notifyDataSetChanged()
        }

        finishRefresh()
    }

    /**
     * 结束刷新或加载
     */
    private fun finishRefresh() {
        if (swipeLoadLayout.isRefreshing) {
            swipeLoadLayout.isRefreshing = false
        }

        if (swipeLoadLayout.isLoadingMore) {
            swipeLoadLayout.isLoadingMore = false
        }
    }

    override fun onFail(msg: String?) {
        showToast(msg)
        finishRefresh()
    }

    override fun getViewHolder(): ViewHolder<ForecastBean.DataBean> {
        return SamplesHolder()
    }

    private class SamplesHolder : ViewHolder<ForecastBean.DataBean>() {

        lateinit var tvText: TextView
        lateinit var ivImage: ImageView

        override fun getLayoutId(): Int {
            return R.layout.tt_list_item
        }

        override fun initView(itemView: View?, data: ForecastBean.DataBean?, context: Context?) {
            tvText = findViewById(R.id.tv_name)
            ivImage = findViewById(R.id.iv_image)
        }

        override fun bindData(data: ForecastBean.DataBean?, position: Int, context: Context?) {
            tvText.text = data!!.title
            ImageUtils.loadImg(data.imgUrl, ivImage)
        }
    }
}