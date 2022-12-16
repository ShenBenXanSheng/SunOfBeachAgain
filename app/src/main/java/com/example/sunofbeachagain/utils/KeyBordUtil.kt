package com.example.sunofbeachagain.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBordUtil {

    fun showKeyBord(context: Context,view:View){
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyBord(context: Context,view: View){
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}