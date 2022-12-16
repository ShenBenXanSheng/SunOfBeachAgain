package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.UserFollowOrFansAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentUserCenterFansBinding
import com.example.sunofbeachagain.repository.UserFollowAndFansRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserCenterFansViewModel
import kotlinx.coroutines.flow.collectLatest


class UserCenterFansFragment(val userId: String?, val token: String?) :
    BaseFragmentViewModel<UserCenterActivity, UserCenterFansViewModel>() {

    override fun getMyCurrentActivity() = requireActivity() as UserCenterActivity

    private lateinit var fragmentUserCenterFansBinding: FragmentUserCenterFansBinding

    override fun getSuccessView(): View {
        fragmentUserCenterFansBinding = FragmentUserCenterFansBinding.inflate(layoutInflater)

        return fragmentUserCenterFansBinding.root
    }

    private var currentUserId: String = ""

    private var currentToken: String = ""

    private val userFollowOrFansAdapter by lazy {
        UserFollowOrFansAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    override fun initView() {

        setToolBarTheme(true, null, null, null)
        userId?.let {
            currentUserId = it
        }

        token?.let {
            currentToken = it

        }
        fragmentUserCenterFansBinding.userCenterFansRv.adapter =
            userFollowOrFansAdapter.withLoadStateFooter(loadMoreAdapter)
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            lifecycleScope.launchWhenCreated {
                getUserFollowOrData(currentToken, currentUserId, "粉丝").collectLatest {

                    userFollowOrFansAdapter.submitData(it)
                }
            }

            userFollowAndFansRepository.userFollowAndFansLoadState.observe(this@UserCenterFansFragment) {
                when (it) {
                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    UserFollowAndFansRepository.UserFollowAndFansLoadStatus.EMPTY -> {
                        ToastUtil.setText("没有更多了")
                    }

                    else -> {}
                }
            }
        }


    }

    override fun initListener() {
        super.initListener()
        userFollowOrFansAdapter.setOnFollowOrFansClickListener(object
            : UserFollowOrFansAdapter.OnFollowOrFansClickListener {
            override fun onFollowOrFansAvatarClick(userId: String) {
                val intent =
                    Intent(currentActivity, UserCenterActivity::class.java)
                intent.putExtra(Constant.SOB_USER_ID, userId)


                    intent.putExtra(Constant.SOB_TOKEN, token)


                startActivity(intent)
            }

        })
    }

    override fun getCurrentViewModel() = UserCenterFansViewModel::class.java


}