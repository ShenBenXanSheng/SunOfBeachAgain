package com.example.sunofbeachagain.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.login.LoginActivity
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.databinding.FragmentBaseBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.NetWorkUtils
import com.example.sunofbeachagain.viewmodel.LoginViewModel
import java.util.*

abstract class BaseFragment<A : Activity> : Fragment() {
    private lateinit var fragmentBaseBinding: FragmentBaseBinding

    private lateinit var loadingView: View
    private lateinit var successView: View
    private lateinit var emptyView: View
    private lateinit var errorView: View

    private lateinit var baseFragmentContainer: FrameLayout

    lateinit var currentActivity: A

    val loginViewModel by lazy {
        ViewModelProvider(requireActivity())[LoginViewModel::class.java]
    }

    enum class FragmentLoadViewStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    fun switchDispatchLoadViewState(fragmentLoadViewStatus: FragmentLoadViewStatus) {
        when (fragmentLoadViewStatus) {
            FragmentLoadViewStatus.LOADING -> {
                hideAllView(loadingView)
            }

            FragmentLoadViewStatus.SUCCESS -> {
                hideAllView(successView)
            }

            FragmentLoadViewStatus.ERROR -> {
                hideAllView(errorView)
            }

            FragmentLoadViewStatus.EMPTY -> {
                hideAllView(emptyView)
            }
        }
    }

    var firstToken = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = getMyCurrentActivity()

        if (currentActivity is HomeActivity) {
            currentActivity.intent.getStringExtra(Constant.SOB_TOKEN)?.let { firstToken = it }
        }



    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        val rootView = setRootView()
        baseFragmentContainer = rootView.findViewById(R.id.base_fragment_container)
        loadAllView()
        hideAllView(null)

        initView()

        initListener()
        initDataListener()
        return rootView
    }

    abstract fun getMyCurrentActivity(): A

    private fun loadAllView() {
        loadingView = loadView(R.layout.state_loading)
        baseFragmentContainer.addView(loadingView)

        successView = getSuccessView()
        baseFragmentContainer.addView(successView)

        emptyView = loadView(R.layout.state_empty)
        emptyView.setOnClickListener {
            emptyReLoad()
        }
        baseFragmentContainer.addView(emptyView)

        errorView = loadView(R.layout.state_error)

        errorView.setOnClickListener {
            errorReLoad()
        }
        baseFragmentContainer.addView(errorView)
    }

    open fun emptyReLoad() {

    }

    open fun errorReLoad() {

    }

    private fun hideAllView(visibleView: View?) {
        loadingView.visibility = View.GONE
        successView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE

        if (visibleView != null) {
            visibleView.visibility = View.VISIBLE
        }
    }

    private fun loadView(layoutId: Int): View {


        return LayoutInflater.from(requireContext()).inflate(layoutId, baseFragmentContainer, false)
    }

    open fun setRootView(): View {
        fragmentBaseBinding = FragmentBaseBinding.inflate(layoutInflater)
        return fragmentBaseBinding.root
    }

    abstract fun getSuccessView(): View

    fun setToolBarTheme(isGone: Boolean, background: Int?, title: String?, titleColor: Int?) {
        if (this::fragmentBaseBinding.isInitialized) {

            fragmentBaseBinding.apply {
                if (isGone) {
                    baseFragmentToolbar.visibility = View.GONE
                }

                if (background != null) {
                    baseFragmentToolbar.background = resources.getDrawable(background, null)
                }

                if (title != null) {
                    baseFragmentToolbarTitle.text = title
                }

                if (titleColor != null) {
                    baseFragmentToolbarTitle.setTextColor(ContextCompat.getColor(requireContext(),
                        titleColor))
                }
            }
        }
    }


    val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val preToken = it.data?.getStringExtra(Constant.SOB_TOKEN)
                .toString()

            loginViewModel.checkToken(if (preToken.isNullOrEmpty()) {
                firstToken
            } else {
                preToken
            })


        }


    fun getTokenAgain() {
        val intent = Intent(currentActivity, LoginActivity::class.java)

        currentActivity.getSharedPreferences(Constant.SOB_LOGIN_SP_KEY,
            AppCompatActivity.MODE_PRIVATE).edit().clear().apply()

        intent.putExtra(Constant.SOB_GET_TOKEN_AGAIN, true)

        registerForActivityResult.launch(intent)

    }


    open fun initDataListener() {

    }

    open fun initListener() {

    }

    abstract fun initView()

}