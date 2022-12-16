package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.UserShouCangAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserShouCangBinding
import com.example.sunofbeachagain.domain.body.ShouCangActionBody
import com.example.sunofbeachagain.repository.UserShouCangRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserShouCansViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class UserShouCansActivity : BaseActivityViewModel<UserShouCansViewModel>() {
    private lateinit var activityUserShouCangBinding: ActivityUserShouCangBinding


    override fun getSuccessView(): View {
        activityUserShouCangBinding = ActivityUserShouCangBinding.inflate(layoutInflater)
        return activityUserShouCangBinding.root
    }

    private val userShouCangAdapter by lazy {
        UserShouCangAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private var currentEssayUrl: String = ""

    private var currentEssayTitle: String = ""

    override fun initView() {

        setMyTAG(this::class.simpleName)
        setToolBarTheme(true, null, false)

        userShouCangAdapter.getIsShouCang(intent.getBooleanExtra(Constant.SOB_IS_SHOUCHANG, false))

        intent.getStringExtra(Constant.SOB_ESSAY_ID)?.let {
            currentEssayUrl = it
        }

        intent.getStringExtra(Constant.SOB_ESSAY_TITLE)?.let {
            currentEssayTitle = it
        }



        activityUserShouCangBinding.apply {
            userShoucangRv.adapter = userShouCangAdapter.withLoadStateFooter(loadMoreAdapter)

            userShoucangSmartRefresh.setRefreshHeader(MaterialHeader(this@UserShouCansActivity))
        }
    }

    override fun onRestart() {
        super.onRestart()
        loginViewModel.checkTokenResultLiveData.observe(this@UserShouCansActivity) {
            if (it != null) {
                if (it.checkTokenBean.success) {
                    currentViewModel?.getUserShouCangData(it.token)
                }
            }
        }
    }

    override fun errorReload() {
        super.errorReload()
        currentViewModel?.getUserShouCangData(firsttoken)
    }

    override fun initDataListener() {
        super.initDataListener()
        activityUserShouCangBinding.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@UserShouCansActivity) {
                if (it != null) {
                    if (it.checkTokenBean.success) {
                        currentViewModel?.getUserShouCangData(it.token)

                        userCreateShoucangTv.setOnClickListener { v ->
                            val intent = Intent(this@UserShouCansActivity,
                                UserCreateShouCangActivity::class.java)

                            intent.putExtra(Constant.SOB_TOKEN, it.token)

                            registerForActivityResult.launch(intent)
                        }
                    }
                }

                userShoucangSmartRefresh.setOnRefreshListener { ref ->
                    if (it != null) {
                        if (it.checkTokenBean.success) {
                            currentViewModel?.getUserShouCangData(it.token)
                        }
                    }

                    ref.finishRefresh()
                }


                userShouCangAdapter.setOnShouCangItemClickListener(object :
                    UserShouCangAdapter.OnShouCangItemClickListener {
                    override fun onShouCangItemClick(shouCangId: String, name: String) {
                        if (it != null) {
                            if (it.checkTokenBean.success) {
                                val intent = Intent(this@UserShouCansActivity,
                                    UserShouCansDetailActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, it.token)

                                intent.putExtra(Constant.SOB_SHOUCANG_ID, shouCangId)

                                intent.putExtra(Constant.SOB_SHOUCANG_NAME, name)

                                registerForActivityResult.launch(intent)

                            }
                        }
                    }

                    override fun onShCangNewItemClick(shouCangId: String) {
                        Log.d(TAG, "收藏内容标题${currentEssayTitle}")
                        Log.d(TAG, "收藏内容Url${currentEssayUrl}")
                        Log.d(TAG, "收藏夹Id${shouCangId}")

                        if (it != null) {
                            if (it.checkTokenBean.success) {

                                val shouCangActionBody = ShouCangActionBody(shouCangId,currentEssayTitle,currentEssayUrl,0.toString(),null)

                                currentViewModel?.postShouCang(it.token,shouCangActionBody)
                            }
                        }
                    }
                })
            }

            currentViewModel?.apply {
                shouCangListLiveData.observe(this@UserShouCansActivity) {
                    lifecycleScope.launchWhenCreated {
                        it.collectLatest {
                            userShouCangAdapter.submitData(it)
                        }
                    }
                }

                userShouCangRepository.userShouCangLoadSate.observe(this@UserShouCansActivity) {
                    when (it) {
                        UserShouCangRepository.UserShouCangLoadStatus.LOADING -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                        }

                        UserShouCangRepository.UserShouCangLoadStatus.SUCCESS -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                        }

                        UserShouCangRepository.UserShouCangLoadStatus.ERROR -> {
                            switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                        }

                        UserShouCangRepository.UserShouCangLoadStatus.LOAD_EMPTY -> {
                            ToastUtil.setText("暂无更多数据")
                        }
                    }
                }

                postShouCangLiveData.observe(this@UserShouCansActivity){
                    if (it.success){
                        setTokenToPreActivity()
                        finish()
                        ToastUtil.setText("收藏成功!")
                    }
                }


            }
        }
    }

    override fun initListener() {
        super.initListener()
        activityUserShouCangBinding.apply {
            userShoucangToolbar.setNavigationOnClickListener {
                setTokenToPreActivity()
                finish()
            }
        }
    }

    override fun getCurrentViewModel() = UserShouCansViewModel::class.java
}