package com.example.sunofbeachagain.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunofbeachagain.databinding.FragmentMoyuListBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlin.math.abs

class MoYuListNestedScrollView : NestedScrollView {
    private var headHeight = 0

    private var scrollSize = 0

    lateinit var smartRefreshLayout: SmartRefreshLayout

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context,
        attributeSet,
        defStyle)


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        if (scrollSize < headHeight) {
            scrollBy(dx, dy)
            consumed[0] = dx
            consumed[1] = dy


            smartRefreshLayout.setEnableNestedScroll(false)
        } else {
            smartRefreshLayout.setEnableNestedScroll(true)

        }

        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }


    fun getHeadViewHeight(measuredHeight: Int) {
        headHeight = measuredHeight
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {

        scrollSize = t

        super.onScrollChanged(l, t, oldl, oldt)

    }

    private var lastX = 0f
    private var lastY = 0f

    //  smrefresh和nv的冲突
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            val x = ev.x
            val y = ev.y

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {

                    if (abs(x - lastX) < abs(y - lastY)) {
                        if (scrollSize == 0) {

                            parent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            parent.requestDisallowInterceptTouchEvent(true)
                        }

                    }

                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                }
            }

            lastX = x
            lastY = y

        }
        return super.dispatchTouchEvent(ev)
    }

    fun getSmartRefresh(listBinding: FragmentMoyuListBinding) {
        smartRefreshLayout = listBinding.moyuSmartRefresh
        smartRefreshLayout.setEnableNestedScroll(false)

    }

}