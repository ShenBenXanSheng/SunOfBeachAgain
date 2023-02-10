package com.example.sunofbeachagain.activity.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.databinding.ActivityEnrollBinding
import com.example.sunofbeachagain.domain.body.EnrollSobBody
import com.example.sunofbeachagain.domain.body.PhoneCodeBody
import com.example.sunofbeachagain.utils.*
import com.example.sunofbeachagain.viewmodel.EnrollViewModel
import java.util.*

class EnrollActivity : AppCompatActivity() {
    private lateinit var activityEnrollBinding: ActivityEnrollBinding

    private var verifyCodeText = ""

    private var phoneNumber = ""

    private var phoneVerifyCode = ""

    private var passWord = ""

    private var nickname = ""

    var getVerifyCodeCountdown = 60

    var timer: Timer? = null

    var timerTask: TimerTask? = null

    private val enrollViewModel by lazy {
        ViewModelProvider(this)[EnrollViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEnrollBinding = ActivityEnrollBinding.inflate(layoutInflater)
        setContentView(activityEnrollBinding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        activityEnrollBinding.apply {
            initView()
            initListener()
            initDataListener()
        }

        loadVerificationCodeImage()
    }

    private fun ActivityEnrollBinding.initView() {
        MyAnimUtil.setAnim(
            this@EnrollActivity,
            enrollVerifyCodeContainer,
            enrollPhoneContainer,
            enrollPhoneCodeInputEt,
            enrollPasswordInputEt,
            enrollPasswordNicknameEt,
            enrollEnrollBt,
            enrollAgreementContainer

        )
    }


    private fun ActivityEnrollBinding.initListener() {
        enrollVerifyCodeIv.setOnClickListener {
            loadVerificationCodeImage()
        }

        enrollToolbar.setNavigationOnClickListener {
            finish()
        }


        enrollGetPhoneCode.setOnClickListener {

            verifyCodeText = enrollVerifyCodeInputEt.text.toString().trim()
            phoneNumber = enrollPhoneInputEt.text.toString().trim()
            if (verifyCodeText.isEmpty() && phoneNumber.isEmpty()) {
                ToastUtil.setText("请输入验证码或电话号")
            } else {
                if (!StringUtil.isPhone(phoneNumber)) {
                    ToastUtil.setText("这不是一个正确的手机号码")
                } else {
                    val phoneCodeBody = PhoneCodeBody(phoneNumber, verifyCodeText)

                    enrollViewModel.getPhoneCode(phoneCodeBody)

                    timer = Timer()
                    timerTask = PhoneCodeTimeTask()

                    timer?.schedule(timerTask, 0, 1000)
                }
            }

        }

        enrollEnrollBt.setOnClickListener {
            phoneNumber = enrollPhoneInputEt.text.toString().trim()

            phoneVerifyCode = enrollPhoneCodeInputEt.text.toString().trim()

            passWord = enrollPasswordInputEt.text.toString().trim()


            nickname = enrollPasswordNicknameEt.text.toString().trim()

            if (phoneNumber.isEmpty() || phoneVerifyCode.isEmpty() || passWord.isEmpty() || nickname.isEmpty()) {
                ToastUtil.setText("请输入信息")
            } else {
                if (!enrollCb.isChecked) {
                    ToastUtil.setText("请勾选协议")
                } else {

                    Log.d("TAG", "手机号:${phoneNumber}")
                    Log.d("TAG", "手机验证码:${phoneVerifyCode}")
                    Log.d("TAG", "密码:${passWord}")
                    Log.d("TAG", "用户名:${nickname}")


                    enrollViewModel.checkPhoneCodeIsTrue(phoneNumber, phoneVerifyCode)
                }
            }

            loadVerificationCodeImage()
        }

    }


    private fun ActivityEnrollBinding.initDataListener() {
        enrollViewModel.apply {
            phoneCodeLiveData.observe(this@EnrollActivity) {

                if (!it.success) {
                    loadVerificationCodeImage()
                    enrollVerifyCodeInputEt.setText("")
                }
                ToastUtil.setText(it.message)
            }

            checkPhoneCodeLiveData.observe(this@EnrollActivity) {
                Log.d("TAG", it.toString())

                if (it.success) {
                    val enrollSobBody =
                        EnrollSobBody(phoneNumber, MD5Util.getMD5String(passWord), nickname)
                    enrollSob(phoneVerifyCode, enrollSobBody)
                }
            }

            enrollSobLiveData.observe(this@EnrollActivity) {
                Log.d("TAG", it.toString())

                if (it.success) {
                    val intent = intent
                    intent.putExtra(Constant.SOB_ENROLL_PHONE_NUMBER, phoneNumber)
                    setResult(RESULT_OK, intent)
                    finish()
                }
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

            .into(activityEnrollBinding.enrollVerifyCodeIv)
    }

    inner class PhoneCodeTimeTask : TimerTask() {
        override fun run() {
            getVerifyCodeCountdown--
            runOnUiThread {
                activityEnrollBinding.enrollGetPhoneCode.text = "(${getVerifyCodeCountdown})秒后再获取"
                activityEnrollBinding.enrollGetPhoneCode.isEnabled = false
                activityEnrollBinding.enrollGetPhoneCode.setTextColor(ContextCompat.getColor(this@EnrollActivity,
                    R.color.gray2))
                if (getVerifyCodeCountdown == 0) {
                    timerTask?.cancel()

                    timerTask = null

                    timer?.cancel()
                    timer = null
                    getVerifyCodeCountdown = 10
                    activityEnrollBinding.enrollGetPhoneCode.isEnabled = true
                    activityEnrollBinding.enrollGetPhoneCode.text = "获取验证码"
                    activityEnrollBinding.enrollGetPhoneCode.setTextColor(ContextCompat.getColor(
                        this@EnrollActivity,
                        R.color.mainColor))
                }
            }
        }

    }


}