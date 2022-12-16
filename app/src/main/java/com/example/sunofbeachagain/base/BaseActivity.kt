package com.example.sunofbeachagain.base

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.login.LoginActivity
import com.example.sunofbeachagain.databinding.ActivityBaseBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.viewmodel.LoginViewModel

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var loadingView: View

    private lateinit var successView: View

    private lateinit var errorView: View

    private lateinit var emptyView: View

    var TAG = ""
    val baseActivityBinding by lazy {
        ActivityBaseBinding.inflate(layoutInflater)
    }

    val loginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private lateinit var loginSp: SharedPreferences

    enum class ActivityLoadViewStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    fun switchDispatchLoadViewState(activityLoadViewStatus: ActivityLoadViewStatus) {
        when (activityLoadViewStatus) {
            ActivityLoadViewStatus.LOADING -> {
                hideAllView(loadingView)
            }

            ActivityLoadViewStatus.SUCCESS -> {
                hideAllView(successView)
            }


            ActivityLoadViewStatus.ERROR -> {
                hideAllView(errorView)
            }


            ActivityLoadViewStatus.EMPTY -> {
                hideAllView(emptyView)
            }
        }
    }

    var firsttoken = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setRootView())
        loadAllView()
        hideAllView(null)


        intent.getStringExtra(Constant.SOB_TOKEN)?.let {
            firsttoken = it
        }

        Log.d("TAG", "firstToken:${firsttoken}")




        loginViewModel.checkToken(firsttoken)


        loginSp = getSharedPreferences(Constant.SOB_LOGIN_SP_KEY, MODE_PRIVATE)

        initView()
        initListener()
        initDataListener()

    }


    fun setMyTAG(simpleName: String?) {
        if (simpleName != null) {
            TAG = simpleName
        }
    }

    //隐藏所有状态布局
    private fun hideAllView(visibleView: View?) {
        loadingView.visibility = View.GONE
        successView.visibility = View.GONE
        errorView.visibility = View.GONE
        emptyView.visibility = View.GONE

        if (visibleView != null) {
            visibleView.visibility = View.VISIBLE
        }
    }

    //加载所有状态布局
    private fun loadAllView() {
        baseActivityBinding.apply {
            loadingView = loadView(R.layout.state_loading)
            baseActivityContainer.addView(loadingView)

            successView = getSuccessView()
            baseActivityContainer.addView(successView)

            emptyView = loadView(R.layout.state_empty)

            emptyView.setOnClickListener {
                emptyReload()
            }


            baseActivityContainer.addView(emptyView)

            errorView = loadView(R.layout.state_error)

            errorView.setOnClickListener {
                errorReload()
            }

            baseActivityContainer.addView(errorView)
        }

    }

    open fun emptyReload() {

    }

    open fun errorReload() {

    }

    private fun loadView(layoutId: Int): View {
        return LayoutInflater.from(this@BaseActivity)
            .inflate(layoutId, baseActivityBinding.baseActivityContainer, false)
    }

    //直接隐藏
    fun setToolBarTheme(isGone: Boolean, stateBarColor: Int?, isFontColorDark: Boolean) {
        if (isGone) {
            baseActivityBinding.baseToolbar.visibility = View.GONE
        }

        if (stateBarColor != null) {
            window.statusBarColor = ContextCompat.getColor(this@BaseActivity, stateBarColor)
        }

        if (isFontColorDark) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    //根据页面需求修改toolbar
    fun setToolBarTheme(
        title: String,
        icon: Int?,
        toolbarBackground: Int?,
        titleColor: Int?,
        isFontColorDark: Boolean,
    ) {
        baseActivityBinding.apply {


            baseToolbarTitle.text = title

            if (icon != null) {
                baseToolbar.navigationIcon = resources.getDrawable(icon, null)

                baseToolbar.setNavigationOnClickListener {
                    setTokenToPreActivity()
                    finish()
                }
            }

            if (toolbarBackground != null) {
                baseToolbar.background =
                    resources.getDrawable(toolbarBackground, null)

                window.statusBarColor = ContextCompat.getColor(this@BaseActivity, toolbarBackground)


            }
            if (isFontColorDark) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }


            if (titleColor != null) {
                baseToolbarTitle.setTextColor(ContextCompat.getColor(this@BaseActivity, titleColor))
            }


        }
    }

    val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            loginViewModel.checkTokenResultLiveData.observe(this) { checkToken ->

                if (checkToken != null) {
                    if (!checkToken.checkTokenBean.success) {
                        loginViewModel.checkToken(it.data?.getStringExtra(Constant.SOB_TOKEN)
                            .toString())
                    }

                } else {


                    loginViewModel.checkToken(it.data?.getStringExtra(Constant.SOB_TOKEN)
                        .toString())
                }
            }


        }

    fun getTokenAgain() {
        val intent = Intent(this, LoginActivity::class.java)

        getSharedPreferences(Constant.SOB_LOGIN_SP_KEY, MODE_PRIVATE).edit().clear().apply()

        intent.putExtra(Constant.SOB_GET_TOKEN_AGAIN, true)

        registerForActivityResult.launch(intent)

    }

    //根据页面需求是否覆盖base界面
    open fun setRootView(): View {
        return baseActivityBinding.root
    }

    //获取加载成功界面
    abstract fun getSuccessView(): View


    open fun initListener() {

    }

    open fun initDataListener() {

    }

    abstract fun initView()

    override fun onBackPressed() {
        setTokenToPreActivity()

        super.onBackPressed()

    }

    fun setTokenToPreActivity() {
        loginViewModel.checkTokenResultLiveData.observe(this) { checkToken ->
            if (checkToken != null) {
                if (checkToken.checkTokenBean.success) {
                    val intent = intent

                    intent.putExtra(Constant.SOB_TOKEN, checkToken.token)


                } else {
                    intent.putExtra(Constant.SOB_TOKEN, firsttoken)
                }
            } else {
                intent.putExtra(Constant.SOB_TOKEN, firsttoken)
            }

            setResult(RESULT_OK, intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.checkTokenResultLiveData.observe(this) { checkToken ->
            if (checkToken != null) {
                if (checkToken.checkTokenBean.success) {
                    loginSp.edit().putString(Constant.SOB_TOKEN, checkToken.token).apply()
                } else {
                    loginSp.edit().putString(Constant.SOB_TOKEN, firsttoken).apply()
                }
            } else {
                loginSp.edit().putString(Constant.SOB_TOKEN, firsttoken).apply()
            }
        }
    }
}