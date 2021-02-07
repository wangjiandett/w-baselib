package com.moa.rxdemo.mvp.view.demons

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.base.ui.adapter.HolderAdapter
import com.moa.baselib.base.ui.adapter.ViewHolder
import com.moa.baselib.utils.FileUtil
import com.moa.baselib.utils.GsonHelper
import com.moa.baselib.utils.Hz2Pinying
import com.moa.baselib.view.SideBar
import com.moa.rxdemo.R


/**
 * slide demo
 *
 * @author wangjian
 */
class SideBarFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_side
    }

    override fun initView(view: View) {
        super.initView(view)

        val listView = findViewById<ListView>(R.id.list_view)
        val sideBar = findViewById<SideBar>(R.id.right_side)

        val citys = FileUtil.readStringFromRaw(activity, R.raw.citys);
        var list = GsonHelper.toList(citys, City::class.java)

        // 遍历添加拼音字符
        list.forEach {
            it.sort = Hz2Pinying.oneHzFirstPy(it.name?.first().toString())
        }

        // 排序
        list.sortBy { it.sort }

        val myAdapter = MyAdapter(listView.context)
        listView.run {
            adapter = myAdapter
            myAdapter.setListAndNotify(list as MutableList<City>)
        }

        // 滑动到指定位置
        sideBar.setOnTouchingLetterChangedListener {
            listView.setSelection(myAdapter.dataList.indexOf(City().apply { sort = it }))
        }
    }

    class City {
//        "code": "1302",
//        "name": "唐山市",
//        "provinceCode": "13"
        var code: String? = null;
        var name: String? = null;
        var provinceCode: String? = null;
        var sort: String? = null;

        /**
         * 重新equals和hashCode方便获取到index，之后自动滚动到指定位置
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as City

            if (sort != other.sort) return false

            return true
        }

        override fun hashCode(): Int {
            return sort?.hashCode() ?: 0
        }
    }

    class MyAdapter(mContext: Context) : HolderAdapter<City>(mContext) {

        /**
         * 要实现adapter使用多个布局即多个holder
         * 必须重写getViewTypeCount和getItemViewType 2个函数
         * 否则容易导致界面错乱
         */
        override fun getViewTypeCount(): Int {
            return 2
        }

        override fun getItemViewType(position: Int): Int {
            val pos = position - 1
            if (pos <= count - 1) {
                // 当position 为0的时候 pos为-1
                // 第一条数据显示A
                // 剩下的与上一条数据的sort不同的时候才显示排序的view
                return if (pos == -1 || getItem(position)?.sort != getItem(pos).sort) {
                     1
                } else {
                     0
                }
            }
            return 0
        }

        override fun createHolder(position: Int, data: City?): ViewHolder<City> {
            return if (getItemViewType(position) == 0) {
                MyHolder(this)
            }else{
                MyHolder2(this)
            }
        }
    }

    class MyHolder(mAdapter: HolderAdapter<City>?) : ViewHolder<City>(mAdapter) {

        var textView: TextView? = null

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }

        override fun initView(itemView: View?, data: City?, context: Context?) {
            textView = findViewById(android.R.id.text1)
        }

        override fun bindData(data: City?, position: Int, context: Context?) {
            textView?.text = data?.name
        }
    }

    class MyHolder2(mAdapter: HolderAdapter<City>?) : ViewHolder<City>(mAdapter) {

        var textView: TextView? = null
        var textView2: TextView? = null

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_2
        }

        override fun initView(itemView: View?, data: City?, context: Context?) {
            textView = findViewById(android.R.id.text1)
            textView2 = findViewById(android.R.id.text2)
        }

        override fun bindData(data: City?, position: Int, context: Context?) {
            textView?.text = data?.sort
            textView2?.text = data?.name
        }
    }
}
