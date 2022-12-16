package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.UserFollowOrFansAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserFansBinding
import com.example.sunofbeachagain.repository.UserFollowAndFansRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.viewmodel.UserCenterFansViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class UserFansOrFollowActivity : BaseActivityViewModel<UserCenterFansViewModel>() {
    private lateinit var activityUserFansBinding: ActivityUserFansBinding
    override fun getSuccessView(): View {
        activityUserFansBinding = ActivityUserFansBinding.inflate(layoutInflater)
        return activityUserFansBinding.root
    }

    private val userFollowOrFansAdapter by lazy {
        UserFollowOrFansAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private var currentUserId = ""

    private var currentState = ""
    override fun initView() {

        setToolBarTheme(if (currentState == "粉丝") {
            "粉丝列表"
        } else {
            "关注列表"
        }, R.mipmap.back, R.color.mainColor, R.color.white, false)

        currentUserId = intent.getStringExtra(Constant.SOB_USER_ID).toString()

        currentState = intent.getStringExtra(Constant.SOB_FANS_OR_FOLLOW_STATE).toString()

        activityUserFansBinding.apply {
            userFansRv.adapter = userFollowOrFansAdapter.withLoadStateFooter(loadMoreAdapter)

            userFansSmartRefresh.setRefreshHeader(MaterialHeader(this@UserFansOrFollowActivity))
        }
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initDataListener() {
        super.initDataListener()

        loginViewModel.checkTokenResultLiveData.observe(this) { checkToken ->
            currentViewModel?.apply {
                if (checkToken != null) {
                    if (checkToken.checkTokenBean.success) {
                        lifecycleScope.launchWhenCreated {
                            getUserFollowOrData(checkToken.token,
                                currentUserId,
                                if (currentState == "粉丝") {
                                    "粉丝"
                                } else {
                                    "关注"
                                }).collectLatest {
                                userFollowOrFansAdapter.submitData(it)
                            }
                        }
                    }
                }


                activityUserFansBinding.userFansSmartRefresh.setOnRefreshListener {
                    if (checkToken != null) {
                        if (checkToken.checkTokenBean.success) {
                            lifecycleScope.launchWhenCreated {
                                getUserFollowOrData(checkToken.token,
                                    currentUserId,
                                    if (currentState == "粉丝") {
                                        "粉丝"
                                    } else {
                                        "关注"
                                    }).collectLatest {
                                    userFollowOrFansAdapter.submitData(it)
                                }
                            }
                        }
                    }

                    it.finishRefresh()
                }
            }

            userFollowOrFansAdapter.setOnFollowOrFansClickListener(object
                : UserFollowOrFansAdapter.OnFollowOrFansClickListener {
                override fun onFollowOrFansAvatarClick(userId: String) {
                    val intent =
                        Intent(this@UserFansOrFollowActivity, UserCenterActivity::class.java)
                    intent.putExtra(Constant.SOB_USER_ID, userId)

                    if (checkToken != null) {
                        intent.putExtra(Constant.SOB_TOKEN, checkToken.token)
                    }

                    startActivity(intent)
                }

            })
        }

        currentViewModel?.apply {
            userFollowAndFansRepository.userFollowAndFansLoadState.observe(this@UserFansOrFollowActivity) {
                when (it) {
                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                    }

                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.EMPTY -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.EMPTY)
                    }

                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                    }

                    else -> {}
                }
            }
        }
    }

    override fun getCurrentViewModel() = UserCenterFansViewModel::class.java
}