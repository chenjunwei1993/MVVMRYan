package com.android.mvvmryan.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


/**
 *@author chenjunwei
 *@desc
 *@date 2019-12-05
 */
class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
        return ViewModelProviders.of(activity).get(cls)
    }

    private object SingletonHolder {
        val holder = ViewModelFactory()
    }

    companion object {
        val INSTANCE = SingletonHolder.holder
    }
}
