package com.android.mvvmryan.binding.viewadapter.smartrefresh


import androidx.databinding.BindingAdapter
import com.android.mvvmryan.binding.command.BindingCommand
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *@author chenjunwei
 *@desc SmartRefreshLayout
 *@date 2019-10-31
 */
object ViewAdapter {
    /**
     * 下拉刷新
     *
     * @param smartRefreshLayout
     * @param onRefreshCommand
     */
    @JvmStatic
    @BindingAdapter("onRefreshCommand")
    fun onRefreshCommand(smartRefreshLayout: SmartRefreshLayout, onRefreshCommand: BindingCommand<*>?) {
        smartRefreshLayout.setOnRefreshListener {
            onRefreshCommand?.execute()
            smartRefreshLayout.finishRefresh()
        }
    }

    /**
     * 加载更多
     *
     * @param smartRefreshLayout
     * @param onLoadMoreCommand
     */
    @JvmStatic
    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(smartRefreshLayout: SmartRefreshLayout, onLoadMoreCommand: BindingCommand<*>?) {
        smartRefreshLayout.setOnLoadMoreListener {
            onLoadMoreCommand?.execute()
            smartRefreshLayout.finishLoadMore()
        }
    }

    /**
     * 加载完成
     *
     * @param smartRefreshLayout
     * @param complete
     */
    @JvmStatic
    @BindingAdapter("onCompleteCommand")
    fun onCompleteCommand(smartRefreshLayout: SmartRefreshLayout, complete: Boolean) {
        if (complete) {
            smartRefreshLayout.post {
                smartRefreshLayout.finishLoadMore()
                smartRefreshLayout.finishRefresh()
            }
        }
    }

}
