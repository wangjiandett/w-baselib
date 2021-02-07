package com.moa.rxdemo.mvp.view.my

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.moa.baselib.base.ui.BaseFragment
import com.moa.baselib.utils.SimpleTextWatcher
import com.moa.rxdemo.R

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/11/24 16:07
 */
class SingleInputFragment : BaseFragment() {

    companion object {
        /**
         * 修改昵称
         */
        const val MODIFY_NICK_NAME = 1
    }

    private lateinit var mEtInput: EditText
    private lateinit var mIvDelete: ImageView
    private lateinit var mTvSubText: TextView
    private lateinit var mBtnSubmit: LinearLayout

    /**
     * 用于区分跳转界面类型
     */
    var type = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_single_input
    }

    override fun initView(view: View) {
        setTitle("")
        mEtInput = findViewById<EditText>(R.id.et_input)
        mIvDelete = findViewById<ImageView>(R.id.iv_delete)
        mTvSubText = findViewById<TextView>(R.id.tv_sub_text)
        mBtnSubmit = findViewById<LinearLayout>(R.id.btn_submit)

        mEtInput.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mIvDelete.visibility = if (TextUtils.isEmpty(s)) View.GONE else View.VISIBLE
            }
        })

        mTvSubText.setText(R.string.modify_confirm)
        setOnClickListener(mBtnSubmit, mIvDelete)
    }

    override fun initData() {
        arguments?.apply {
            type = getInt("type", type)
        }
    }

    override fun onClick(v: View?) {
        if (v == mBtnSubmit) {
            val text = mEtInput.text.toString()
            if (!TextUtils.isEmpty(text)) {
                val intent = Intent()
                intent.putExtra("result", text)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        } else if (v == mIvDelete) {
            mEtInput.setText("")
        }
    }
}