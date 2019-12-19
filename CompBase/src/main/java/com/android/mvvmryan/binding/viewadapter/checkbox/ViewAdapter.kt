package com.android.mvvmryan.binding.viewadapter.checkbox

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand


/**
 *@author chenjunwei
 *@desc CheckBox 绑定监听
 *@date 2019-10-30
 */
object ViewAdapter {
    /**
     * @param bindingCommand
     */
    @JvmStatic
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean>) {
        checkBox.setOnCheckedChangeListener { _, b -> bindingCommand.execute(b) }
    }
}

