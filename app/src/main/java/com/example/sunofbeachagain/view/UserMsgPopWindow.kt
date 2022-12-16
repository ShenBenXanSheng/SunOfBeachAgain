package com.example.sunofbeachagain.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.PopUserMsgBinding

class UserMsgPopWindow(context: Context,width:Int) :
    PopupWindow(width, LayoutParams.WRAP_CONTENT) {

    private lateinit var onPopUserSexClickListener: OnPopUserSexClickListener

    private val popUserMsgBinding by lazy {
        PopUserMsgBinding.inflate(LayoutInflater.from(context),null)
    }

        init {
            isOutsideTouchable = true
            animationStyle = R.style.pop_anim

            contentView = popUserMsgBinding.root
            popUserMsgBinding.initListener()
        }

    private fun PopUserMsgBinding.initListener(){
        popUserFemale.setOnClickListener {
            if (this@UserMsgPopWindow::onPopUserSexClickListener.isInitialized) {
                onPopUserSexClickListener.onUserSexClick(popUserFemale.text.toString(),"1")
            }

            dismiss()
        }


        popUserMale.setOnClickListener {
            if (this@UserMsgPopWindow::onPopUserSexClickListener.isInitialized) {
                onPopUserSexClickListener.onUserSexClick(popUserMale.text.toString(),"0")
            }
            dismiss()
        }

        popUserCoders.setOnClickListener {
            if (this@UserMsgPopWindow::onPopUserSexClickListener.isInitialized) {
                onPopUserSexClickListener.onUserSexClick(popUserCoders.text.toString(),"2")
            }
            dismiss()
        }
    }

    fun setOnPopUserSexClickListener(onPopUserSexClickListener: OnPopUserSexClickListener){
        this.onPopUserSexClickListener = onPopUserSexClickListener
    }

    interface OnPopUserSexClickListener{
        fun onUserSexClick(text:String,code:String)
    }
}