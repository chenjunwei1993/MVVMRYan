package com.android.mvvmryan.binding.viewadapter.recyclerview


import androidx.recyclerview.widget.RecyclerView

/**
 *@author chenjunwei
 *@desc
 *@date 2019-10-31
 */
class LineManagers private constructor() {

    interface LineManagerFactory {
        fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration
    }

    companion object {

        fun both(): LineManagerFactory {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.BOTH)
                }
            }
        }

        fun horizontal(): LineManagerFactory {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.HORIZONTAL)
                }
            }
        }

        fun vertical(): LineManagerFactory {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.VERTICAL)
                }
            }
        }
    }
}
