package com.moa.rxdemo.mvp.view.base;


import android.graphics.Typeface
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.base.ui.adapter.PagesAdapter
import com.moa.rxdemo.R


/**
 * 封装顶部tab和顶部ViewPager页面上层功能，自动切换tab样式。继承此类只需关注addItemPages和addItemPageTabTitle 这2个方法即可
 *
 * @author wangjian
 * Created on 2020/10/6 15:09
 */
abstract class BaseTabPagerFragment : BaseFragment() {

    private lateinit var mRadioGroup: RadioGroup
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var mAdapter: PagesAdapter
    private lateinit var mPageList: ArrayList<BaseFragment>
    private lateinit var mPageTabTitle: ArrayList<String>

    var currentPosition = 0

    override fun initView(view: View) {
        mRadioGroup = findViewById<RadioGroup>(R.id.rg_tab_layout)
        mViewPager = findViewById<androidx.viewpager.widget.ViewPager>(R.id.view_pager)
        mAdapter = PagesAdapter(childFragmentManager)
        mViewPager.adapter = mAdapter

        mViewPager.addOnPageChangeListener(pageChangeListener)
        mRadioGroup.setOnCheckedChangeListener(checkChangeListener)
    }

    override fun initData() {
        mPageList = arrayListOf()
        mPageTabTitle = arrayListOf()
        addItemPages(mPageList)
        mAdapter.setFragments(mPageList as List<androidx.fragment.app.Fragment>?)
        mAdapter.notifyDataSetChanged()

        initTabs()
    }

    private val pageChangeListener: SimpleOnPageChangeListener = object : SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            currentPosition = position
            mRadioGroup.setOnCheckedChangeListener(null)
            mRadioGroup.check(mRadioGroup.getChildAt(position).id)
            mRadioGroup.setOnCheckedChangeListener(checkChangeListener)
            updateTabTextSize()
        }
    }

    private val checkChangeListener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        mViewPager.removeOnPageChangeListener(pageChangeListener)
        val checkRadio = group.findViewById<RadioButton>(checkedId)
        currentPosition = group.indexOfChild(checkRadio)
        mViewPager.setCurrentItem(currentPosition, false)
        mViewPager.addOnPageChangeListener(pageChangeListener)

        updateTabTextSize()
    }

    private fun initTabs() {
        mRadioGroup.removeAllViews()
        addItemPageTabTitle(mPageTabTitle)
        for ((index, item) in mPageTabTitle.withIndex()) {
            // 此处必须传递mRadioGroup作为root 否则导致无法加载布局样式
            val radioButton = LayoutInflater.from(activity).inflate(R.layout.tab_radio_btn, mRadioGroup, false) as RadioButton
            // TextViewCompat.setTextAppearance(radioButton, R.style.race_tab_item_style)
            radioButton.text = item
            mRadioGroup.addView(radioButton)
        }

        if (mRadioGroup.childCount > 0) {
            (mRadioGroup.getChildAt(0) as RadioButton).isChecked = true
        }

        updateTabTextSize()
    }

    /**
     * 更新选中的tab字体大小
     */
    private fun updateTabTextSize() {
        for (index in 0 until mRadioGroup.childCount) {
            val childView = mRadioGroup.getChildAt(index) as RadioButton
            if (mRadioGroup.checkedRadioButtonId == childView.id) {
                childView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                childView.setTypeface(null, Typeface.BOLD)
            } else {
                childView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                childView.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    fun setCurrentItem(index: Int){
        mViewPager.setCurrentItem(index, true)
    }

    /**
     * 添加item界面
     */
    abstract fun addItemPages(pageList: ArrayList<BaseFragment>)

    /**
     * 添加顶部的tab title
     */
    abstract fun addItemPageTabTitle(pageTabTitle: ArrayList<String>)
}

