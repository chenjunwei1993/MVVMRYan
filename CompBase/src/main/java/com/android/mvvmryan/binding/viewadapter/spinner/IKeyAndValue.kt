package com.android.mvvmryan.binding.viewadapter.spinner

/**
 *@author chenjunwei
 *@desc 下拉Spinner控件的键值对, 实现该接口,返回key,value值, 在xml绑定List<IKeyAndValue>
 *@date 2019-10-31
 */
interface IKeyAndValue {
    val key: String

    val value: String
}
