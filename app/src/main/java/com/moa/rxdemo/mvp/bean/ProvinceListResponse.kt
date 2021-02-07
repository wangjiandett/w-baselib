package com.moa.rxdemo.mvp.bean;

import com.moa.rxdemo.mvp.view.demons.pickerview.bean.IDataChoose

/**
 * ProvinceListResponse
 *
 * @author wangjian
 * Created on 2020/12/23 9:54
 */
class ProvinceItem : IDataChoose {
    var id = 0
    var name: String? = null
    var createDate: String? = null
    var status: String? = null

    constructor(id: Int, name: String?) {
        this.id = id
        this.name = name
    }

    constructor()

    override fun getTitle(): String? {
        return name
    }

    override fun getId(): Int? {
        return id
    }
}