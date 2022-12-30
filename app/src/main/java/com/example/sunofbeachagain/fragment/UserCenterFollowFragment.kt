package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.UserFollowOrFansAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentUserCenterFollowBinding
import com.example.sunofbeachagain.domain.CheckTokenDataHasToken
import com.example.sunofbeachagain.repository.UserFollowAndFansRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserCenterFollowViewModel
import kotlinx.coroutines.flow.collectLatest

class UserCenterFollowFragment(val userId: String?, val token: String?) :
    BaseFragmentViewModel<UserCenterActivity, UserCenterFollowViewModel>() {


    override fun getMyCurrentActivity() = requireActivity() as UserCenterActivity

    private lateinit var fragmentUserCenterFollowBinding: FragmentUserCenterFollowBinding

    override fun getSuccessView(): View {
        fragmentUserCenterFollowBinding = FragmentUserCenterFollowBinding.inflate(layoutInflater)

        return fragmentUserCenterFollowBinding.root
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
        fragmentUserCenterFollowBinding.userCenterFollowRv.adapter =
            userFollowOrFansAdapter.withLoadStateFooter(loadMoreAdapter)
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@UserCenterFollowFragment) {checkToken->

                setDataToAdapter(checkToken)

                currentViewModel.followLiveData.observe(this@UserCenterFollowFragment){
                    if (it.success){
                        setDataToAdapter(checkToken)
                    }

                    ToastUtil.setText(it.message)
                }
            }
            userFollowAndFansRepository.userFollowAndFansLoadState.observe(this@UserCenterFollowFragment) {
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

    private fun UserCenterFollowViewModel.setDataToAdapter(
        checkToken: CheckTokenDataHasToken?,
    ) {
        lifecycleScope.launchWhenCreated {
            getUserFollowOrData(currentToken, currentUserId, "关注").collectLatest {

                userFollowOrFansAdapter.submitData(it)
            }
        }

        if (checkToken != null) {
            userFollowOrFansAdapter.getToken(checkToken.checkTokenBean)
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

            override fun onFollowOrFansFollowAndUnfollowClick(followState: Int, userId: String) {
                when(followState){
                    0->{
                        currentViewModel.followUser(currentToken,userId)
                    }

                    1->{
                        currentViewModel.followUser(currentToken,userId)
                    }

                    2->{
                        currentViewModel.unfollowUser(currentToken,userId)
                    }

                    3->{
                        currentViewModel.unfollowUser(currentToken,userId)
                    }
                }
            }

        })
    }

    override fun getCurrentViewModel() = UserCenterFollowViewModel::class.java

}