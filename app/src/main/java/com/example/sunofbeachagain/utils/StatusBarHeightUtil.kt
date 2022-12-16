package com.example.sunofbeachagain.utils

import android.content.Context

object StatusBarHeightUtil {
    private var mStatusBarHeight = 0
    fun getStatusBarHeight(context: Context): Int {
        if (mStatusBarHeight > 0) {
            return mStatusBarHeight
        }
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        mStatusBarHeight = statusBarHeight
        return mStatusBarHeight
    }

}