package com.example.sunofbeachagain.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class SobMoYuViewPager : ViewPager {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    private var lastX = 0f
    private var lastY = 0f

    //vp和vp2的冲突
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            val x = ev.x
            val y = ev.y

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    parent.requestDisallowInterceptTouchEvent(false)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (abs(x - lastX) > abs(y - lastY)) {
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

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    stopAdd()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    startAdd()
                }
                else -> {}
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAdd()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAdd()
    }

    private var canBeAdd = false
    private val runnable = object : Runnable {
        override fun run() {
            if (canBeAdd) {
                currentItem++
            }
            currentItem = currentItem
            postDelayed(this, 4000)
        }

    }

    private fun startAdd() {
        canBeAdd = true
        postDelayed(runnable, 4000)
    }

    private fun stopAdd() {
        canBeAdd = false
        removeCallbacks(runnable)
    }
}