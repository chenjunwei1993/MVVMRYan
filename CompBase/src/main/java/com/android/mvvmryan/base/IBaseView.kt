package com.android.mvvmryan.base


/**
 * @author chenjunwei
 * @desc
 * @date 2019-10-28
 */
interface IBaseView {
    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()
}
