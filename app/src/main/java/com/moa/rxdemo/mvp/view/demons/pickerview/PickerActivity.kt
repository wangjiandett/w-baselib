package com.moa.rxdemo.mvp.view.demons.pickerview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.moa.baselib.base.ui.BaseActivity
import com.moa.rxdemo.R
import com.moa.rxdemo.mvp.view.demons.pickerview.bean.CardBean
import com.moa.rxdemo.mvp.view.demons.pickerview.bean.ProvinceBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * 类或文件描述
 *
 * Created by：wangjian on 2018/12/24 14:23
 */
class PickerActivity : BaseActivity() {

    private val options1Items: ArrayList<ProvinceBean> = ArrayList<ProvinceBean>()
    private val options2Items = ArrayList<ArrayList<String>>()

    private var btn_Options: Button? = null
    private var btn_CustomOptions: Button? = null
    private var btn_CustomTime: Button? = null

    private var pvTime: TimePickerView? = null
    private var pvCustomTime: TimePickerView? = null
    private var pvCustomLunar: TimePickerView? = null
    private var pvOptions: OptionsPickerView<Any>? = null
    private var pvCustomOptions: OptionsPickerView<Any>? = null
    private var pvNoLinkOptions: OptionsPickerView<Any>? = null
    private val cardItem: ArrayList<CardBean> = ArrayList<CardBean>()

    private val food = ArrayList<String>()
    private val clothes = ArrayList<String>()
    private val computer = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.tt_activity_pickerview
    }

    override fun initView() {
        super.initView()
        //等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃。
        getOptionData()

        initTimePicker()
        initCustomTimePicker()
        initLunarPicker()
        initOptionPicker()
        initCustomOptionPicker()
        initNoLinkOptionsPicker()

        val btn_Time = findViewById<View>(R.id.btn_Time) as Button
        btn_Options = findViewById<View>(R.id.btn_Options) as Button
        btn_CustomOptions = findViewById<View>(R.id.btn_CustomOptions) as Button
        btn_CustomTime = findViewById<View>(R.id.btn_CustomTime) as Button
        val btn_no_linkage = findViewById<View>(R.id.btn_no_linkage) as Button
        val btn_to_Fragment = findViewById<View>(R.id.btn_fragment) as Button
        val btn_circle = findViewById<View>(R.id.btn_circle) as Button


        btn_Time.setOnClickListener(this)
        btn_Options!!.setOnClickListener(this)
        btn_CustomOptions!!.setOnClickListener(this)
        btn_CustomTime!!.setOnClickListener(this)
        btn_no_linkage.setOnClickListener(this)
        btn_to_Fragment.setOnClickListener(this)
        btn_circle.setOnClickListener(this)

        findViewById<View>(R.id.btn_GotoJsonData).setOnClickListener(this)
        findViewById<View>(R.id.btn_lunar).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.btn_Time && pvTime != null) {
            // pvTime.setDate(Calendar.getInstance());
            /* pvTime.show(); //show timePicker*/
            pvTime!!.show(v) //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (v.id == R.id.btn_Options && pvOptions != null) {
            pvOptions!!.show() //弹出条件选择器
        } else if (v.id == R.id.btn_CustomOptions && pvCustomOptions != null) {
            pvCustomOptions!!.show() //弹出自定义条件选择器
        } else if (v.id == R.id.btn_CustomTime && pvCustomTime != null) {
            pvCustomTime!!.show() //弹出自定义时间选择器
        } else if (v.id == R.id.btn_no_linkage && pvNoLinkOptions != null) { //不联动数据选择器
            pvNoLinkOptions!!.show()
        } else if (v.id == R.id.btn_GotoJsonData) { //跳转到 省市区解析示例页面
            startActivity(Intent(this, JsonDataActivity::class.java))
        } else if (v.id == R.id.btn_lunar) {
            pvCustomLunar!!.show()
        } else if (v.id == R.id.btn_circle) {
            startActivity(Intent(this, TestCircleWheelViewActivity::class.java))
        }
    }

    /**
     * 农历时间已扩展至 ： 1900 - 2100年
     */
    private fun initLunarPicker() {
        val selectedDate = Calendar.getInstance() //系统当前时间
        val startDate = Calendar.getInstance()
        startDate[2014, 1] = 23
        val endDate = Calendar.getInstance()
        endDate[2069, 2] = 28
        //时间选择器 ，自定义布局
        pvCustomLunar = TimePickerBuilder(this, OnTimeSelectListener { date, v -> //选中事件回调
            showToast(getTime(date))
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, object : CustomListener {
                    override fun customLayout(v: View) {
                        val tvSubmit = v.findViewById<View>(R.id.tv_finish) as TextView
                        val ivCancel = v.findViewById<View>(R.id.iv_cancel) as ImageView
                        tvSubmit.setOnClickListener {
                            pvCustomLunar!!.returnData()
                            pvCustomLunar!!.dismiss()
                        }
                        ivCancel.setOnClickListener { pvCustomLunar!!.dismiss() }
                        //公农历切换
                        val cb_lunar = v.findViewById<View>(R.id.cb_lunar) as CheckBox
                        cb_lunar.setOnCheckedChangeListener { buttonView, isChecked ->
                            pvCustomLunar!!.isLunarCalendar = !pvCustomLunar!!.isLunarCalendar
                            //自适应宽
                            setTimePickerChildWeight(v, if (isChecked) 0.8f else 1f, if (isChecked) 1f else 1.1f)
                        }
                    }

                    /**
                     * 公农历切换后调整宽
                     * @param v
                     * @param yearWeight
                     * @param weight
                     */
                    private fun setTimePickerChildWeight(v: View, yearWeight: Float, weight: Float) {
                        val timePicker = v.findViewById<View>(R.id.timepicker) as ViewGroup
                        val year = timePicker.getChildAt(0)
                        val lp = year.layoutParams as LinearLayout.LayoutParams
                        lp.weight = yearWeight
                        year.layoutParams = lp
                        for (i in 1 until timePicker.childCount) {
                            val childAt = timePicker.getChildAt(i)
                            val childLp = childAt.layoutParams as LinearLayout.LayoutParams
                            childLp.weight = weight
                            childAt.layoutParams = childLp
                        }
                    }
                })
                .setType(booleanArrayOf(true, true, true, false, false, false))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build()
    }


    private fun initTimePicker() { //Dialog 模式下，在底部弹出
        pvTime = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
            showToast(getTime(date))
            Log.i("pvTime", "onTimeSelect")
        })
                .setTimeSelectChangeListener { Log.i("pvTime", "onTimeSelectChanged") }
                .setType(booleanArrayOf(true, true, true, true, true, true))
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener { Log.i("pvTime", "onCancelClickListener") }
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build()
        val mDialog = pvTime?.getDialog()
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM)
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime?.getDialogContainerLayout()?.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
    }

    private fun initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        val selectedDate = Calendar.getInstance() //系统当前时间
        val startDate = Calendar.getInstance()
        startDate[2014, 1] = 23
        val endDate = Calendar.getInstance()
        endDate[2027, 2] = 28
        //时间选择器 ，自定义布局
        pvCustomTime = TimePickerBuilder(this, OnTimeSelectListener { date, v -> //选中事件回调
            btn_CustomTime!!.text = getTime(date)
        }) /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               / *.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time) { v ->
                    val tvSubmit = v.findViewById<View>(R.id.tv_finish) as TextView
                    val ivCancel = v.findViewById<View>(R.id.iv_cancel) as ImageView
                    tvSubmit.setOnClickListener {
                        pvCustomTime!!.returnData()
                        pvCustomTime!!.dismiss()
                    }
                    ivCancel.setOnClickListener { pvCustomTime!!.dismiss() }
                }
                .setContentTextSize(18)
                .setType(booleanArrayOf(false, false, false, true, true, true))
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(-0xdb5263)
                .build()
    }

    private fun initOptionPicker() { //条件选择器初始化
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v -> //返回的分别是三个级别的选中位置
            val tx = (options1Items[options1].pickerViewText
                    + options2Items[options1][options2] /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/)
            btn_Options!!.text = tx
        })
                .setTitleText("城市选择")
                .setContentTextSize(20) //设置滚轮文字大小
                .setDividerColor(Color.LTGRAY) //设置分割线的颜色
                .setSelectOptions(0, 1) //默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true) //切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener { options1, options2, options3 ->
                    val str = "options1: $options1\noptions2: $options2\noptions3: $options3"
                    showToast(str)
                }
                .build<Any>()

//        pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        (pvOptions as OptionsPickerView<Any>?)?.setPicker(options1Items as List<Any>?, options2Items as List<MutableList<Any>>?) //二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }

    private fun initCustomOptionPicker() { //条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, option2, options3, v -> //返回的分别是三个级别的选中位置
            val tx = cardItem[options1].pickerViewText
            btn_CustomOptions!!.text = tx
        })
                .setLayoutRes(R.layout.pickerview_custom_options) { v ->
                    val tvSubmit = v.findViewById<View>(R.id.tv_finish) as TextView
                    val tvAdd = v.findViewById<View>(R.id.tv_add) as TextView
                    val ivCancel = v.findViewById<View>(R.id.iv_cancel) as ImageView
                    tvSubmit.setOnClickListener {
                        pvCustomOptions!!.returnData()
                        pvCustomOptions!!.dismiss()
                    }
                    ivCancel.setOnClickListener { pvCustomOptions!!.dismiss() }
                    tvAdd.setOnClickListener {
                        getCardData()
                        pvCustomOptions!!.setPicker(cardItem as List<Nothing>?)
                    }
                }
                .isDialog(true)
                .setOutSideCancelable(false)
                .build<Any>()
        (pvCustomOptions as OptionsPickerView<Any>?)?.setPicker(cardItem as List<Any>?) //添加数据
    }

    private fun initNoLinkOptionsPicker() { // 不联动的多级选项
        pvNoLinkOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            val str = """
                food:${food[options1]}
                clothes:${clothes[options2]}
                computer:${computer[options3]}
                """.trimIndent()
            showToast(str)
        })
                .setOptionsSelectChangeListener { options1, options2, options3 ->
                    val str = "options1: $options1\noptions2: $options2\noptions3: $options3"
                    showToast(str)
                }
                .setItemVisibleCount(5) // .setSelectOptions(0, 1, 1)
                .build<Any>()
        (pvNoLinkOptions as OptionsPickerView<Any>?)?.setNPicker(food as List<Any>?, clothes as List<Any>?, computer as List<Any>?)
        (pvNoLinkOptions as OptionsPickerView<Any>?)?.setSelectOptions(0, 1, 1)
    }

    private fun getTime(date: Date): String? { //可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(date)
    }

    private fun getOptionData() {
        /*
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        getCardData()
        getNoLinkData()

        //选项1
        options1Items.add(ProvinceBean(0, "广东", "描述部分", "其他数据"))
        options1Items.add(ProvinceBean(1, "湖南", "描述部分", "其他数据"))
        options1Items.add(ProvinceBean(2, "广西", "描述部分", "其他数据"))

        //选项2
        val options2Items_01 = ArrayList<String>()
        options2Items_01.add("广州")
        options2Items_01.add("佛山")
        options2Items_01.add("东莞")
        options2Items_01.add("珠海")
        val options2Items_02 = ArrayList<String>()
        options2Items_02.add("长沙")
        options2Items_02.add("岳阳")
        options2Items_02.add("株洲")
        options2Items_02.add("衡阳")
        val options2Items_03 = ArrayList<String>()
        options2Items_03.add("桂林")
        options2Items_03.add("玉林")
        options2Items.add(options2Items_01)
        options2Items.add(options2Items_02)
        options2Items.add(options2Items_03)
        /*--------数据源添加完毕---------*/
    }

    private fun getCardData() {
        for (i in 0..4) {
            cardItem.add(CardBean(i, "No.ABC12345 $i"))
        }
        for (i in cardItem.indices) {
            if (cardItem[i].cardNo.length > 6) {
                val str_item = cardItem[i].cardNo.substring(0, 6) + "..."
                cardItem[i].cardNo = str_item
            }
        }
    }

    private fun getNoLinkData() {
        food.add("KFC")
        food.add("MacDonald")
        food.add("Pizza hut")
        clothes.add("Nike")
        clothes.add("Adidas")
        clothes.add("Armani")
        computer.add("ASUS")
        computer.add("Lenovo")
        computer.add("Apple")
        computer.add("HP")
    }


    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, PickerActivity::class.java);
        }
    }
}