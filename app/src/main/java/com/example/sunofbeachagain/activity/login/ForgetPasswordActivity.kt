package com.example.sunofbeachagain.activity.login

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.ActivityForgetPasswordBinding
import com.example.sunofbeachagain.domain.body.ForgetPasswordBody
import com.example.sunofbeachagain.domain.body.PhoneCodeBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.MD5Util
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.ForgetViewModel

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var activityForgetPasswordBinding: ActivityForgetPasswordBinding

    private var forgetPhoneNumber = ""

    private var forgetVerifyCode = ""

    private var forgetMsgCode = ""

    private var forgetPassword = ""

    private val forgetViewModel by lazy {
        ViewModelProvider(this)[ForgetViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgetPasswordBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(activityForgetPasswordBinding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        loadVerificationCodeImage()

        activityForgetPasswordBinding.apply {
            initView()
            initListener()
            initDataListener()
        }
    }

    private fun ActivityForgetPasswordBinding.initView() {

    }

    private fun ActivityForgetPasswordBinding.initListener() {
        forgerVerifyCodeIv.setOnClickListener {
            loadVerificationCodeImage()
        }

        forgetSendMsgBt.setOnClickListener {
            forgetPhoneNumber = forgerPhoneNumberInputEt.text.toString().trim()

            forgetVerifyCode = forgerVerifyCodeInputEt.text.toString().trim()

            if (forgetPhoneNumber.isNullOrEmpty() && forgetVerifyCode.isNullOrEmpty()){
                ToastUtil.setText("请输入信息")
            }else{
                val phoneCodeBody = PhoneCodeBody(forgetPhoneNumber,forgetVerifyCode)
                forgetViewModel.getPhoneCode(phoneCodeBody)
            }
        }

        forgetConfirmBt.setOnClickListener {
            forgetMsgCode = forgerPhoneCodeInputEt.text.toString().trim()
            forgetPassword = forgerPassWordInputEt.text.toString().trim()

            if (forgetMsgCode.isNullOrEmpty() && forgetPassword.isNullOrEmpty()){
                ToastUtil.setText("请输入信息")
            }else{
                forgetViewModel.checkPhoneCode(forgetPhoneNumber,forgetMsgCode)
            }
        }
    }

    private fun ActivityForgetPasswordBinding.initDataListener() {

        forgetViewModel.apply {
            forgetPhoneNumberLiveData.observe(this@ForgetPasswordActivity){
                Log.d("Forget",it.message)
                if (it.success){
                    forgetSendMsgBt.visibility = View.GONE
                    forgetConfirmBt.visibility = View.VISIBLE
                    forgerPhoneCodeInputEt.visibility = View.VISIBLE
                    forgerPassWordInputEt.visibility = View.VISIBLE

                    ToastUtil.setText(it.message)
                }else{
                    loadVerificationCodeImage()
                }
            }

            checkPhoneCode.observe(this@ForgetPasswordActivity){
                if (it.success){
                    val forgetPasswordBody =
                        ForgetPasswordBody(forgetPhoneNumber, MD5Util.getMD5String(forgetPassword))

                    forgetViewModel.forgetPassword(forgetMsgCode,forgetPasswordBody)
                }
                Log.d("Forget",it.message)
            }

            forgetViewModel.forgetPasswordLiveData.observe(this@ForgetPasswordActivity){
                if (it.success){
                    finish()
                }
                ToastUtil.setText(it.message)
            }
        }

    }

    private fun loadVerificationCodeImage() {
        Glide.with(this)
            .load(
                String.format(
                    Constant.SOB_BASE_URL + "/uc/ut/captcha?code=随机数",
                    System.currentTimeMillis()
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

            .into(activityForgetPasswordBinding.forgerVerifyCodeIv)
    }


}