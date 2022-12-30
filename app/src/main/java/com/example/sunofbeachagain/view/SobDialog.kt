package com.example.sunofbeachagain.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sunofbeachagain.databinding.DialogUserExitOrLoginBinding

class SobDialog : Dialog {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, resId: Int) : super(context, resId)

    private lateinit var onDialogCleanShouCangConfirmClick: OnDialogCleanShouCangConfirmClick

    private lateinit var onDialogDeleteShouCangConfirmClick: OnDialogDeleteShouCangConfirmClick

    private lateinit var onDialogShouCangConfirmClick: OnDialogShouCangConfirmClick

    private var dialogMsgText: String = ""

    private lateinit var onDialogExitConfirmClickListener: OnDialogExitConfirmClickListener

    private lateinit var onDialogLoginConfirmClickListener: OnDialogLoginConfirmClickListener

    private val dialogUserExitOrLoginBinding by lazy {
        DialogUserExitOrLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogUserExitOrLoginBinding.root)
        setCanceledOnTouchOutside(true)

        initListener()

    }

    fun setMsgText(dialogMsgText: String, dialogConfirmText: String) {
        this.dialogMsgText = dialogMsgText
        dialogUserExitOrLoginBinding.apply {
            dialogMsg.text = dialogMsgText

            dialogConfirm.text = dialogConfirmText


            if (dialogMsgText=="已经收藏过了"){
                dialogGiveUp.visibility = View.GONE
            }else{
                dialogGiveUp.visibility = View.VISIBLE
            }
        }


    }

    private fun initListener() {
        dialogUserExitOrLoginBinding.apply {
            dialogConfirm.setOnClickListener {

                when (dialogMsgText) {

                    "还没登陆" -> {
                        if (this@SobDialog::onDialogLoginConfirmClickListener.isInitialized) {
                            onDialogLoginConfirmClickListener.onDialogLoginConfirmClick()
                        }
                    }

                    "真的要退出吗?" -> {
                        if (this@SobDialog::onDialogExitConfirmClickListener.isInitialized) {
                            onDialogExitConfirmClickListener.onDialogExitConfirmClick()
                        }
                    }

                    "确定要收藏吗?" -> {
                        if (this@SobDialog::onDialogShouCangConfirmClick.isInitialized) {
                            onDialogShouCangConfirmClick.onDialogShouCangConfirmClick()
                        }
                    }

                    "真的要删除收藏吗?"->{
                        if (this@SobDialog::onDialogDeleteShouCangConfirmClick.isInitialized) {
                            onDialogDeleteShouCangConfirmClick.onDialogDeleteShouCangClick()
                        }

                    }

                    "真的要清空所有收藏吗?"->{
                        if (this@SobDialog::onDialogCleanShouCangConfirmClick.isInitialized) {
                            onDialogCleanShouCangConfirmClick.onDialogCleanShouCangClick()
                        }
                    }



                }

                dismiss()
            }

            dialogGiveUp.setOnClickListener {
                dismiss()
            }
        }

    }

    fun setOnDialogLoginConfirmClickListener(onDialogLoginConfirmClickListener: OnDialogLoginConfirmClickListener) {
        this.onDialogLoginConfirmClickListener = onDialogLoginConfirmClickListener
    }

    interface OnDialogLoginConfirmClickListener {
        fun onDialogLoginConfirmClick()
    }

    fun setOnDialogExitConfirmClickListener(onDialogExitConfirmClickListener: OnDialogExitConfirmClickListener) {
        this.onDialogExitConfirmClickListener = onDialogExitConfirmClickListener
    }

    interface OnDialogExitConfirmClickListener {
        fun onDialogExitConfirmClick()
    }

    fun setOnDialogShouCangConfirmClick(onDialogShouCangConfirmClick: OnDialogShouCangConfirmClick) {
        this.onDialogShouCangConfirmClick = onDialogShouCangConfirmClick
    }

    interface OnDialogShouCangConfirmClick {
        fun onDialogShouCangConfirmClick()
    }

    fun setOnDialogDeleteShouCangConfirmClick(onDialogDeleteShouCangConfirmClick: OnDialogDeleteShouCangConfirmClick){
        this.onDialogDeleteShouCangConfirmClick = onDialogDeleteShouCangConfirmClick
    }

    interface OnDialogDeleteShouCangConfirmClick{
        fun onDialogDeleteShouCangClick()
    }

    fun setOnDialogCleanShouCangConfirmClick(onDialogCleanShouCangConfirmClick: OnDialogCleanShouCangConfirmClick){
        this.onDialogCleanShouCangConfirmClick = onDialogCleanShouCangConfirmClick
    }

    interface OnDialogCleanShouCangConfirmClick{
        fun onDialogCleanShouCangClick()
    }
}