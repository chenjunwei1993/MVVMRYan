package com.android.mvvmryan.binding.viewadapter.view


import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import java.util.concurrent.TimeUnit

/**
 * @author chenjunwei
 * @desc
 * @date 2019-10-31
 */
object ViewAdapter {
    /**
     * 防重复点击间隔(秒)
     */
    private const val CLICK_INTERVAL = 1

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(view: View, clickCommand: BindingCommand<*>?, isThrottleFirst: Boolean) {
        if (isThrottleFirst) {
            view.clicks().subscribe { clickCommand?.execute() }
        } else {
            //1秒钟内只允许点击1次
            view.clicks()
                    .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.SECONDS)
                    .subscribe { clickCommand?.execute() }
        }
    }

    /**
     * view的onLongClick事件绑定
     */
    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
    fun onLongClickCommand(view: View, clickCommand: BindingCommand<*>?) {
        view.longClicks()
                .subscribe { clickCommand?.execute() }
    }

    /**
     * 回调控件本身
     *
     * @param currentView
     * @param bindingCommand
     */
    @JvmStatic
    @BindingAdapter(value = ["currentView"], requireAll = false)
    fun replyCurrentView(currentView: View, bindingCommand: BindingCommand<View>?) {
        bindingCommand?.execute(currentView)
    }

    /**
     * view是否需要获取焦点
     */
    @JvmStatic
    @BindingAdapter("requestFocus")
    fun requestFocusCommand(view: View, needRequestFocus: Boolean) {
        if (needRequestFocus) {
            view.isFocusableInTouchMode = true
            view.requestFocus()
        } else {
            view.clearFocus()
        }
    }

    /**
     * view的焦点发生变化的事件绑定
     */
    @JvmStatic
    @BindingAdapter("onFocusChangeCommand")
    fun onFocusChangeCommand(view: View, onFocusChangeCommand: BindingCommand<Boolean>?) {
        view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            onFocusChangeCommand?.execute(hasFocus)
        }
    }

    /**
     * view的显示隐藏
     */
    @JvmStatic
    @BindingAdapter(value = ["isVisible"], requireAll = false)
    fun isVisible(view: View, visibility: Boolean) {
        if (visibility) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}
