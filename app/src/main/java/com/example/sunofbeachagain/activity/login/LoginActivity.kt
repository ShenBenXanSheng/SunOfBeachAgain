package com.example.sunofbeachagain.activity.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.databinding.ActivityLoginBinding
import com.example.sunofbeachagain.domain.body.UserBody
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.MD5Util.getMD5String
import com.example.sunofbeachagain.utils.MyAnimUtil
import com.example.sunofbeachagain.utils.NetWorkUtils.*
import com.example.sunofbeachagain.utils.StringUtil
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class LoginActivity : AppCompatActivity() {
    private val activityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val TAG = this::class.java.simpleName

    private val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(220))

    private val loginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }


    private val loginSp by lazy {
        getSharedPreferences(Constant.SOB_LOGIN_SP_KEY, MODE_PRIVATE)
    }

    private val rememberAccountSp by lazy {
        getSharedPreferences("rememberAccountSp", MODE_PRIVATE)
    }

    private val rememberAccountList = mutableListOf<String>()

    private var getTokenAgain = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLoginBinding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        val isLogin = loginSp.getBoolean(Constant.SOB_IS_LOGIN, false)

        val spToken = loginSp.getString(Constant.SOB_TOKEN, "")


        if (isLogin) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra(Constant.SOB_TOKEN, spToken)
            startActivity(intent)
            finish()
        }

        val intent = intent
        getTokenAgain = intent.getBooleanExtra(Constant.SOB_GET_TOKEN_AGAIN, false)


        if (getTokenAgain) {
            activityLoginBinding.loginToolbar.navigationIcon =
                resources.getDrawable(R.mipmap.back, null)
        }

        rememberAccountSp.getStringSet(Constant.SOB_REMEMBER_ACCOUNT, null)?.let {
            val toMutableList = it.toMutableList()

            if (StringUtil.isPhone(toMutableList[0])) {
                loginViewModel.getLoginAvatar(toMutableList[0])
            }

            activityLoginBinding.loginPhoneNumberEd.setText(toMutableList[0])

            activityLoginBinding.loginPasswordEd.setText(toMutableList[1])


            activityLoginBinding.rememberAccountCb.isChecked = true

        }


        initView()
        initListener()
        initDataListener()
    }


    override fun onResume() {
        super.onResume()
        val connected = isConnected(this)
        if (connected) {
            loadVerificationCodeImage()
        } else {
            AlertDialog.Builder(this)
                .setTitle("?????????????????????")
                .setMessage("??????????????????????????????????")
                .setPositiveButton("??????"
                ) { _, _ -> startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                .setNegativeButton("??????"
                ) { dialog, _ -> dialog?.dismiss() }
                .show()

        }
    }

    private fun initView() {

        activityLoginBinding.apply {
            MyAnimUtil.setAnim(this@LoginActivity,
                loginWelcomeTv,
                loginAvatarIv,
                loginPhoneNumberEd,
                loginPasswordEd,
                loginVerifyCodeContainer,
                loginRememberAccountContainer,
                loginLoginBt,
                loginRegisterAndForgerContainer,
                loginUserAgreementContainer)



            loginVisitorLoginTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TextView.addTextWatcherFlow() = callbackFlow<String> {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                trySend(s.toString())
            }

        }

        addTextChangedListener(textWatcher)
        awaitClose { removeTextChangedListener(textWatcher) }
    }.catch {

    }

    private fun initListener() {

        activityLoginBinding.apply {
            lifecycleScope.launchWhenCreated {
                loginPhoneNumberEd.addTextWatcherFlow().collect {
                    if (it.isEmpty()) {
                        Glide.with(loginAvatarIv).load(R.mipmap.paimeng).apply(requestOptions)
                            .into(loginAvatarIv)
                    } else {
                        if (StringUtil.isPhone(it)) {
                            loginViewModel.getLoginAvatar(it)
                        }
                    }
                }
            }

            loginLoginBt.setOnClickListener {
                val phoneNumber = loginPhoneNumberEd.text.toString().trim()

                val password = getMD5String(loginPasswordEd.text.toString().trim())
                if (phoneNumber.isEmpty() || loginPasswordEd.text.toString().trim()
                        .isEmpty()
                ) {
                    ToastUtil.setText("????????????????????????")
                } else {
                    val verifyCode = loginVerifyCodeEd.text.toString().trim()

                    if (verifyCode.isEmpty()) {
                        ToastUtil.setText("??????????????????")
                    } else {

                        if (!loginCb.isChecked) {
                            ToastUtil.setText("?????????????????????")
                        } else {

                            val userBody = UserBody(phoneNumber, password)


                            if (rememberAccountCb.isChecked) {
                                rememberAccountList.add(phoneNumber)

                                rememberAccountList.add(loginPasswordEd.text.toString().trim())

                                rememberAccountSp.edit().putStringSet(Constant.SOB_REMEMBER_ACCOUNT,
                                    rememberAccountList.toMutableSet()).apply()
                            } else {
                                rememberAccountSp.edit().clear().apply()
                            }


                            loginLoginBt.isEnabled = false
                            loginViewModel.postLogin(userBody, verifyCode)
                        }

                    }

                }

            }

            loginVerifyCodeIv.setOnClickListener {
                loadVerificationCodeImage()
            }

            loginVisitorLoginTv.setOnClickListener {
                loginToHome("null")
            }

            loginToolbar.setNavigationOnClickListener {
                loginToHome("null")
            }

            val enrollRegisterForActivityResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    val enrollPhoneNumber =
                        it.data?.getStringExtra(Constant.SOB_ENROLL_PHONE_NUMBER)


                    enrollPhoneNumber?.let { phoneNumber ->
                        loginPhoneNumberEd.setText(phoneNumber)
                        loginPhoneNumberEd.setSelection(phoneNumber.length)
                    }


                }

            loginRegisterTv.setOnClickListener {
                val intent = Intent(this@LoginActivity, EnrollActivity::class.java)
                enrollRegisterForActivityResult.launch(intent)
            }

            loginForgerTv.setOnClickListener {
                val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
                startActivity(intent)
            }

        }


    }

    private fun initDataListener() {
        activityLoginBinding.apply {
            loginViewModel.apply {
                //????????????????????????
                loginAvatarLiveData.observe(this@LoginActivity) {

                    Glide.with(loginAvatarIv).load(it ?: R.mipmap.paimeng).apply(requestOptions)
                        .into(loginAvatarIv)

                }


                //????????????????????????
                loginResultLiveData.observe(this@LoginActivity) {
                    loginLoginBt.isEnabled = true
                    if (!it.success) {
                        loadVerificationCodeImage()
                    } else {
                        //??????token
                        tokenLiveData.observe(this@LoginActivity) { token ->
                            if (token != null) {
                                loginViewModel.checkToken(token)

                                //??????token
                                loginViewModel.checkTokenResultLiveData.observe(this@LoginActivity) { checkTokenResult ->
                                    if (checkTokenResult != null) {
                                        loginToHome(checkTokenResult.token)
                                    }
                                }
                            }

                        }
                    }
                    ToastUtil.setText(it.message)
                }


            }

        }
    }

    private fun loginToHome(token: String?) {
        val edit = loginSp.edit()
        edit.putString(Constant.SOB_TOKEN, token)
        edit.putBoolean(Constant.SOB_IS_LOGIN, true)
        edit.apply()

        val intent =
            Intent(this@LoginActivity, HomeActivity::class.java)

        intent.putExtra(Constant.SOB_TOKEN, token)


        if (!getTokenAgain) {
            startActivity(intent)
        } else {
            setResult(RESULT_OK, intent)
        }

        finish()
    }

    private fun loadVerificationCodeImage() {
        Glide.with(this)
            .load(
                String.format(
                    Constant.SOB_BASE_URL + "/uc/ut/captcha?code=?????????",
                    System.currentTimeMillis()
                )
            )
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

            .into(activityLoginBinding.loginVerifyCodeIv)
    }


    override fun onDestroy() {
        super.onDestroy()
        val edit = loginSp.edit()
        edit.putBoolean(Constant.SOB_IS_LOGIN, true)
        edit.apply()
    }

}