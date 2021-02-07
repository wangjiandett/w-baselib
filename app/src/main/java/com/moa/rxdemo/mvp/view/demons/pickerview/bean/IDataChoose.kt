package com.moa.rxdemo.mvp.view.demons.pickerview.bean

import java.io.Serializable

/**
 * data choose 公共实体
 *
 * @author wangjian
 * Created on 2020/12/22 16:38
 */
interface IDataChoose : Serializable {

    /**
     * 显示的title
     */
    fun getTitle(): String?

    /**
     * 实体id
     */
    fun getId(): Int?

}