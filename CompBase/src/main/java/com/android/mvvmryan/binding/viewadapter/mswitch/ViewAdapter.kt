package com.android.mvvmryan.binding.viewadapter.mswitch

import android.widget.Switch
import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand

/**
 *@author chenjunwei
 *@desc Switch
 *@date 2019-10-30
 */
object ViewAdapter {
    /**
     * 设置开关状态
     *
     * @param mSwitch Switch控件
     */
    @JvmStatic
    @BindingAdapter("switchState")
    fun setSwitchState(mSwitch: Switch, isChecked: Boolean) {
        mSwitch.isChecked = isChecked
    }

    /**
     * Switch的状态改变监听
     *
     * @param mSwitch        Switch控件
     * @param changeListener 事件绑定命令
     */
    @JvmStatic
    @BindingAdapter("onCheckedChangeCommand")
    fun onCheckedChangeCommand(mSwitch: Switch, changeListener: BindingCommand<Boolean>?) {
        if (changeListener != null) {
            mSwitch.setOnCheckedChangeListener { _, isChecked ->  changeListener?.execute(isChecked)}
        }
    }
}
