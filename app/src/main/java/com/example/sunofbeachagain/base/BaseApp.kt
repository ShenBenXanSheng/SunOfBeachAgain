package com.example.sunofbeachagain.base

import android.app.Application
import android.content.Context

class BaseApp : Application() {
    companion object{
        var mContext: Context? = null
    }


    override fun onCreate() {
        super.onCreate()
        mContext = baseContext
    }
}