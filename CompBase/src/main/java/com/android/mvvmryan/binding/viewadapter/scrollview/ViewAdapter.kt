package com.android.mvvmryan.binding.viewadapter.scrollview

import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand


/**
 *@author chenjunwei
 *@desc
 *@date 2019-10-31
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter("onScrollChangeCommand")
    fun onScrollChangeCommand(nestedScrollView: NestedScrollView, onScrollChangeCommand: BindingCommand<NestScrollDataWrapper>?) {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            onScrollChangeCommand?.execute(NestScrollDataWrapper(scrollX, scrollY, oldScrollX, oldScrollY))
        })
    }

    @JvmStatic
    @BindingAdapter("onScrollChangeCommand")
    fun onScrollChangeCommand(scrollView: ScrollView, onScrollChangeCommand: BindingCommand<ScrollDataWrapper>?) {
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            onScrollChangeCommand?.execute(ScrollDataWrapper(scrollView.scrollX.toFloat(), scrollView.scrollY.toFloat()))
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float)

    class NestScrollDataWrapper(var scrollX: Int, var scrollY: Int, var oldScrollX: Int, var oldScrollY: Int)
}
