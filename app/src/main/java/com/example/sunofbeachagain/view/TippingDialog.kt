package com.example.sunofbeachagain.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.sunofbeachagain.databinding.DialogTippingBinding

class TippingDialog : Dialog {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, resId: Int) : super(context, resId)

    private lateinit var onTippingDialogClickListener: OnTippingDialogClickListener

    private var maxSob: Int = 0

    private var tippingSob = 2

    private  val dialogTippingBinding: DialogTippingBinding by lazy {
        DialogTippingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(dialogTippingBinding.root)
        setCanceledOnTouchOutside(true)

        dialogTippingBinding.apply {
            initView()
            initListener()
        }
    }

    private fun DialogTippingBinding.initView() {
        tippingMaxText.text = maxSob.toString()

        tippingSeekBar.max = maxSob


    }

    private fun DialogTippingBinding.initListener() {
        tippingSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                tippingSob = tippingSeekBar.progress
                tippingCount.text = tippingSeekBar.progress.toString()

                tippingAdd.isEnabled = tippingSeekBar.progress != maxSob
                tippingJian.isEnabled = tippingSeekBar.progress != 2

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        tippingAdd.setOnClickListener {
            var i = (tippingCount.text.toString().toInt())

            if ((i >= maxSob)) {
                tippingAdd.isEnabled = false
            }else{
                tippingAdd.isEnabled = true
                i++

                tippingSeekBar.progress = i
                if (i>=2){
                    tippingJian.isEnabled = true
                }

                tippingCount.text = i.toString()
            }

        }

        tippingJian.setOnClickListener {
            val i = (tippingCount.text.toString().toInt())

            if (i<=2){
                tippingJian.isEnabled = false
            }else{
                tippingJian.isEnabled = true
                if (i<=maxSob){
                    tippingAdd.isEnabled = true
                }
                val finil = i-1

                tippingSeekBar.progress = finil
                tippingCount.text = finil.toString()
            }
        }

        dialogTipping.setOnClickListener {
            if (this@TippingDialog::onTippingDialogClickListener.isInitialized) {
                onTippingDialogClickListener.onTippingClick(tippingSob)
                dismiss()
            }
        }
    }

    fun getMaxSob(maxSob: Int) {
        this.maxSob = maxSob
    }

    fun setOnTippingDialogClickListener(onTippingDialogClickListener: OnTippingDialogClickListener){
        this.onTippingDialogClickListener = onTippingDialogClickListener
    }

    interface OnTippingDialogClickListener{
        fun onTippingClick(tippingSob:Int)
    }
}