package com.example.sunofbeachagain.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText

/**
 * 拦截键盘向下的 EditTextView
 */
@SuppressLint("AppCompatCustomView")
class TextEditTextView : AppCompatEditText {
    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == 1) {
            super.onKeyPreIme(keyCode, event)
            onKeyBoardHideListener!!.onKeyHide(keyCode, event)
            return false
        }
        return super.onKeyPreIme(keyCode, event)
    }

    /**
     * 键盘监听接口
     */
    private var onKeyBoardHideListener: OnKeyBoardHideListener? = null

    fun setOnKeyBoardHideListener(onKeyBoardHideListener: OnKeyBoardHideListener?) {
        this.onKeyBoardHideListener = onKeyBoardHideListener
    }

    interface OnKeyBoardHideListener {
        fun onKeyHide(keyCode: Int, event: KeyEvent?)
    }
}