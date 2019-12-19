package com.android.mvvmryan.base

/**
 *@author chenjunwei
 *@desc RecycleView多布局ItemViewModel是封装
 *@date 2019-12-04
 */
class MultiItemViewModel<VM : BaseViewModel<*>>(viewModel: VM) : ItemViewModel<VM>(viewModel) {
    var itemType: Any? = null
}
