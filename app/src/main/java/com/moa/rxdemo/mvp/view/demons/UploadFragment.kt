package com.moa.rxdemo.mvp.view.demons

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.moa.baselib.base.net.mvp.SimpleValueCallback
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.*
import com.moa.baselib.utils.matisse.GifSizeFilter
import com.moa.baselib.utils.matisse.Glide4Engine
import com.moa.rxdemo.R
import com.moa.rxdemo.mvp.model.DownloadModel
import com.moa.rxdemo.mvp.model.UploadModel
import com.moa.rxdemo.mvp.model.UploadResp
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import java.io.File
import java.util.*

/**
 * 文件上传下载操作
 *
 * Created by：wangjian on 2018/12/20 14:24
 */
class UploadFragment : BaseFragment() {
    private val SELECT_REQUEST_CODE: Int = 2
    lateinit var textTv: TextView
    lateinit var showIv: ImageView

    var path: List<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.tt_fragment_upload
    }


    override fun initView(view: View) {
        super.initView(view)

        PermissionHelper.checkSDcardPermission(null, activity, 0)

        textTv = view.findViewById<TextView>(R.id.tv_name)
        showIv = view.findViewById<ImageView>(R.id.iv_image)


        view.findViewById<View>(R.id.btn_select).setOnClickListener {
            // 知乎图库选择图片
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .theme(R.style.Matisse_Dracula)
                    .countable(false)
                    .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .maxSelectable(2)
                    .originalEnable(true)
                    .maxOriginalSize(10)
                    .imageEngine(Glide4Engine())
                    .forResult(SELECT_REQUEST_CODE)
        }

        view.findViewById<View>(R.id.btn_upload).setOnClickListener {
            val params: MutableMap<String, String> = HashMap()
            params["type"] = "1"

            val list = arrayListOf<File>()
            path?.forEach { it ->
                list.add(File(it))
            }

            UploadModel(object : SimpleValueCallback<UploadResp>() {
                override fun onProgressChange(percent: Int) {
                    LogUtils.d(percent)
                }

                override fun onSuccess(value: UploadResp) {
                    LogUtils.d(value)
                    this@UploadFragment.textTv.text = GsonHelper.toJson(value)
                }

                override fun onFail(msg: String?) {
                    LogUtils.d(msg)
                }
            }).uploadFiles("file", list, params)
        }


        // 下载文件
        initDownload()
        view.findViewById<View>(R.id.btn_download).setOnClickListener {
            if (!downloadModel?.isDownloading!!) {
                downloadModel?.download(Files.getLogFile("xxx.apk"))
            } else {
                showToast("正在下载，请稍后")
            }
        }
        // 取消下载
        view.findViewById<View>(R.id.btn_cancel_download).setOnClickListener {
            downloadModel?.cancel()
        }

        // 选择浏览器或者第三方工具下载
        view.findViewById<View>(R.id.btn_download_browser).setOnClickListener {
            AppUtils.openBrowser(activity, "https://d1.music.126.net/dmusic/NeteaseCloudMusic_Music_official_7.3.0.1599039901.apk");
        }
    }

    var downloadModel: DownloadModel? = null

    private fun initDownload() {
        downloadModel = DownloadModel(object : SimpleValueCallback<File>() {
            override fun onProgressChange(percent: Int) {
                LogUtils.d(percent)
                this@UploadFragment.textTv.text = "下载进度：$percent"
            }

            override fun onSuccess(value: File) {
                this@UploadFragment.textTv.text = "下载文件路径：${value.absolutePath}"
            }

            override fun onFail(msg: String?) {
                LogUtils.d(msg)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_REQUEST_CODE) {
                // uris
                println(Matisse.obtainResult(data!!))
                // paths
                println(Matisse.obtainPathResult(data))

                path = Matisse.obtainPathResult(data)
                path?.run {
                    activity?.applicationContext?.let { Glide.with(it).load(this[0]).into(showIv) }
                }

            }
        }
    }
}