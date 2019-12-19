package com.android.mvvmryan.binding.viewadapter.viewgroup

import androidx.databinding.ViewDataBinding

/**
 * @author chenjunwei
 * @desc
 * @date 2019-10-31
 */
interface IBindingItemViewModel<V : ViewDataBinding> {
    fun injecDataBinding(binding: V)
}
