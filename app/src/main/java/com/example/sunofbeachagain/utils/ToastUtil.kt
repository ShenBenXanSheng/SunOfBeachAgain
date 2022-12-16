package com.example.sunofbeachagain.utils

import android.widget.Toast
import com.example.sunofbeachagain.base.BaseApp

object ToastUtil {
    private var mToast: Toast? = null

    fun setText(text: String) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApp.mContext!!, text, Toast.LENGTH_SHORT)
        }

        mToast?.setText(text)
        mToast?.show()
    }
}