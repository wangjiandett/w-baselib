package com.moa.rxdemo.mvp.view.demons

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.Navigation
import com.moa.baselib.base.ui.BaseListFragment
import com.moa.baselib.base.ui.H5Activity
import com.moa.baselib.base.ui.adapter.ViewHolder
import com.moa.baselib.utils.PermissionHelper
import com.moa.rxdemo.R
import com.moa.rxdemo.mvp.view.demons.pickerview.PickerActivity
import com.moa.rxdemo.mvp.view.demons.recycler.RecyclerViewManagerFragment
import com.moa.rxdemo.mvp.view.demons.recycler.RefreshRecyclerFragment

/**
 * 类或文件描述
 *
 * Created by：wangjian on 2018/12/20 14:42
 */
class SamplesFragment : BaseListFragment<SamplesFragment.SampleItem>() {

    private lateinit var listView: ListView;


    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_samples;
    }

    override fun initHeader() {
        super.initHeader()
        setTitle(R.string.home_tab_title_races)
    }

    override fun initView(view: View) {
        super.initView(view)
        listView = view.findViewById<ListView>(R.id.lv_list) as ListView;
        bindAdapter(listView)

        listView.setOnItemClickListener { parent, view, position, id ->
            val item = parent.adapter.getItem(position) as SampleItem
            item.actionId?.let {

                val bundle = Bundle()

                if (it == R.id.action_2_scan) {
                    // 扫一扫需要震动和相机访问权
                    var hasPerNeedReq = PermissionHelper.checkAndRequestPermissions(this, activity, 1,
                            arrayOf(Manifest.permission.VIBRATE, Manifest.permission.CAMERA))
                    if (hasPerNeedReq) {
                        Navigation.findNavController(view).navigate(it)
                    }
                }
                else {
                    if(it == R.id.action_2_h5){
                        bundle.putSerializable(H5Activity.EXTRA_DATA, H5Activity.H5Request("快递查询", "https://m.kuaidi100" +
                                ".com/index_all.html"))
                    }
                    Navigation.findNavController(view).navigate(it ,bundle)
                }

            }
        }
    }

    override fun initData() {
        super.initData()

        val list = arrayListOf(

                SampleItem(ShotScreenFragment::class.java.simpleName, R.id.action_2_camera),
                SampleItem(ShareFragment::class.java.simpleName, R.id.action_2_share),
                SampleItem(RoomFragment::class.java.simpleName, R.id.action_2_room),
                SampleItem(DispatcherFragment::class.java.simpleName, R.id.action_2_dispathcer),
                SampleItem(ViewsFragment::class.java.simpleName, R.id.action_2_views),
                SampleItem(SwipeRefreshFragment::class.java.simpleName, R.id.action_2_swipe),
                SampleItem(CheckAbleFragment::class.java.simpleName, R.id.action_2_checkable),
                SampleItem(RecyclerViewManagerFragment::class.java.simpleName, R.id.action_2_recycler),
                SampleItem(RefreshRecyclerFragment::class.java.simpleName, R.id.action_2_recycler_refresh),
                SampleItem(ViewPagerFragment::class.java.simpleName, R.id.action_2_view_pager),
                SampleItem(ViewFlipperFragment::class.java.simpleName, R.id.action_2_view_flipper),
                SampleItem(UploadFragment::class.java.simpleName, R.id.action_2_upload),
                SampleItem(MessengerFragment::class.java.simpleName, R.id.action_2_messenger),

                // activity
                SampleItem(CropImageActivity::class.java.simpleName, R.id.action_2_crop),
                SampleItem(ScanActivity::class.java.simpleName, R.id.action_2_scan),
                SampleItem(PickerActivity::class.java.simpleName, R.id.action_2_pickerview),
                SampleItem(H5Activity::class.java.simpleName, R.id.action_2_h5)

        )

        mHolderAdapter.setListAndNotify(list)
    }

    override fun getViewHolder(): ViewHolder<SampleItem> {
        return SamplesHolder()
    }

    class SamplesHolder : ViewHolder<SampleItem>() {

        lateinit var tvText: TextView

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }

        override fun initView(itemView: View?, data: SampleItem?, context: Context?) {
            tvText = findViewById(android.R.id.text1)
        }

        override fun bindData(data: SampleItem?, position: Int, context: Context?) {
            tvText.text = data!!.name
        }
    }

    class SampleItem(name: String, actionId: Int) {
        var name: String? = name;
        var actionId: Int? = actionId;
    }
}