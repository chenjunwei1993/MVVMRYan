package com.android.mvvmryan.binding.viewadapter.radiogroup

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand

/**
 *@author chenjunwei
 *@desc RadioGroup
 *@date 2019-10-30
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun onCheckedChangedCommand(radioGroup: RadioGroup, bindingCommand: BindingCommand<String>) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<View>(checkedId) as RadioButton
            bindingCommand.execute(radioButton.text.toString())
        }
    }
}
