package com.android.mvvmryan.binding.viewadapter.viewpager


import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager

import com.android.mvvmryan.binding.command.BindingCommand

/**
 * Created by goldze on 2017/6/18.
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"], requireAll = false)
    fun onScrollChangeCommand(viewPager: ViewPager,
                              onPageScrolledCommand: BindingCommand<ViewPagerDataWrapper>?,
                              onPageSelectedCommand: BindingCommand<Int>?,
                              onPageScrollStateChangedCommand: BindingCommand<Int>?) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var state: Int = 0
            override fun onPageScrollStateChanged(state: Int) {
                this.state = state
                onPageScrollStateChangedCommand?.execute(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageScrolledCommand?.execute(ViewPagerDataWrapper(position.toFloat(), positionOffset, positionOffsetPixels, state))
            }

            override fun onPageSelected(position: Int) {
                onPageSelectedCommand?.execute(position)
            }

        })

    }

    class ViewPagerDataWrapper(var position: Float, var positionOffset: Float, var positionOffsetPixels: Int, var state: Int)
}
