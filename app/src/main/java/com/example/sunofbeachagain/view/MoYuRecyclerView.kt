package com.example.sunofbeachagain.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class MoYuRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context,
        attributeSet,
        defStyle)


    private var lastX = 0f
    private var lastY = 0f


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            val x = ev.x
            val y = ev.y

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    parent.requestDisallowInterceptTouchEvent(false)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (abs(x - lastX) < abs(y - lastY)) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                else -> {}
            }

            lastX = x
            lastY = y

        }

        return super.dispatchTouchEvent(ev)
    }


}