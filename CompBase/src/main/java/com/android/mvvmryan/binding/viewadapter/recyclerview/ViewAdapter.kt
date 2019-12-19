package com.android.mvvmryan.binding.viewadapter.recyclerview


import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mvvmryan.binding.command.BindingCommand
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 *@author chenjunwei
 *@desc
 *@date 2019-11-01
 */
object ViewAdapter {

    @JvmStatic
    @BindingAdapter("lineManager")
    fun setLineManager(recyclerView: RecyclerView, lineManagerFactory: LineManagers.LineManagerFactory) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView))
    }


    @JvmStatic
    @BindingAdapter(value = ["onScrollChangeCommand", "onScrollStateChangedCommand"], requireAll = false)
    fun onScrollChangeCommand(recyclerView: RecyclerView,
                              onScrollChangeCommand: BindingCommand<ScrollDataWrapper>?,
                              onScrollStateChangedCommand: BindingCommand<Int>?) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var state: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollChangeCommand?.execute(ScrollDataWrapper(dx.toFloat(), dy.toFloat(), state))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state = newState
                onScrollStateChangedCommand?.execute(newState)
            }
        })

    }

    @JvmStatic
    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(recyclerView: RecyclerView, onLoadMoreCommand: BindingCommand<Int>) {
        val listener = OnScrollListener(onLoadMoreCommand)
        recyclerView.addOnScrollListener(listener)
    }

    @JvmStatic
    @BindingAdapter("itemAnimator")
    fun setItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator) {
        recyclerView.itemAnimator = animator
    }

    @SuppressLint("CheckResult")
    class OnScrollListener(private val onLoadMoreCommand: BindingCommand<Int>?) : RecyclerView.OnScrollListener() {

        private val methodInvoke = PublishSubject.create<Int>()

        init {
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe { integer -> onLoadMoreCommand?.execute(integer) }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            layoutManager?.run {
                val visibleItemCount = childCount
                val totalItemCount = itemCount
                val pastVisibleItems = findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    if (onLoadMoreCommand != null) {
                        methodInvoke.onNext(recyclerView.adapter!!.itemCount)
                    }
                }
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }


    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)
}
