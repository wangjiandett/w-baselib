package com.moa.rxdemo.mvp.view.demons

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.FileUtil
import com.moa.baselib.utils.ShareUtils
import com.moa.rxdemo.R
import java.io.File
import java.util.*


/**
 * 分享demo
 *
 * Created by：wangjian on 2018/12/21 15:58
 */
class ShareFragment : BaseFragment() {

    private var container: LinearLayout? = null
    private var btnShareText: Button? = null
    private var btnShareFile: Button? = null
    private var btnShareFiles: Button? = null
    private var btnShareTextQq: Button? = null
    private var btnShareFileQq: Button? = null
    private var btnShareTextWx: Button? = null
    private var btnShareFileWx: Button? = null

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_share
    }

    override fun initView(view: View) {
        super.initView(view)
        view.let {
            btnShareText = findViewById<View>(R.id.btn_share_text) as Button
            btnShareText!!.setOnClickListener {
                ShareUtils.shareText(activity, "系统分享的 hello")
            }

            btnShareFile = findViewById<View>(R.id.btn_share_file) as Button
            btnShareFile!!.setOnClickListener {
                ShareUtils.shareFile(activity, getShareFile())
            }

            btnShareFiles = findViewById<View>(R.id.btn_share_files) as Button
            btnShareFiles!!.setOnClickListener {

                val list = mutableListOf<File>()
                list.add(getShareFile())
                list.add(getShareFile())
                ShareUtils.shareFiles(activity, list as ArrayList<File>?)
            }

            btnShareTextQq = findViewById<View>(R.id.btn_share_text_qq) as Button
            btnShareTextQq!!.setOnClickListener {
                ShareUtils.shareText2QQ(activity, "分享到qq的：hello")
            }

            btnShareFileQq = findViewById<View>(R.id.btn_share_file_qq) as Button
            btnShareFileQq!!.setOnClickListener {
                ShareUtils.shareFile2QQ(activity, getShareFile())
            }

            btnShareTextWx = findViewById<View>(R.id.btn_share_text_wx) as Button
            btnShareTextWx!!.setOnClickListener {
                ShareUtils.shareText2WX(activity, "分享到qq的：hello")
            }

            btnShareFileWx = findViewById<View>(R.id.btn_share_file_wx) as Button
            btnShareFileWx!!.setOnClickListener {
                ShareUtils.shareFile2WX(activity, getShareFile())
            }
        }
    }

    fun getShareFile() : File{
        val desFile = FileUtil.createFile(activity?.cacheDir?.absolutePath, "province.txt")
        FileUtil.copyAssetsFile(activity, "province.json",desFile)
        return desFile
    }



}