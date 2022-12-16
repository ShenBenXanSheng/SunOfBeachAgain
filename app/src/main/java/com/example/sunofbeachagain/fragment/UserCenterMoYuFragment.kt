package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.moyu.MoYuDetailActivity
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MoYuListAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentUserCenterMoYuBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.repository.UserCenterMoYuRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.NetWorkUtils
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserCenterMoYuViewModel
import kotlinx.coroutines.flow.collectLatest


class UserCenterMoYuFragment(val userId: String?, val token: String?) :
    BaseFragmentViewModel<UserCenterActivity, UserCenterMoYuViewModel>() {


    private lateinit var fragmentUserCenterMoYuBinding: FragmentUserCenterMoYuBinding

    private val userCenterMoYuViewModel by lazy {
        ViewModelProvider(requireActivity())[UserCenterMoYuViewModel::class.java]
    }

    private var currentUserId = ""

    private var currentToken = ""

    private val moYuListAdapter by lazy {
        MoYuListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }


    override fun initView() {
        userId?.let {
            currentUserId = it
        }

        token?.let {
            currentToken = it
        }

        //  Log.d("UserCenterMoYuFragment", "UserCenterMoYuToken${currentToken.toString()}")


        setToolBarTheme(true, null, null, null)

        userCenterMoYuViewModel.getUserCenterMoYuData(currentUserId)

        moYuListAdapter.getContentLine(3)

        fragmentUserCenterMoYuBinding.userCenterMoyuRv.adapter =
            moYuListAdapter.withLoadStateFooter(loadMoreAdapter)
    }


    override fun initDataListener() {
        super.initDataListener()
        userCenterMoYuViewModel.apply {
            userCenterMoYuLiveData.observe(viewLifecycleOwner) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        moYuListAdapter.submitData(it)
                    }
                }
            }

            userCenterMoYuRepository.userCenterMoYuLoadStateLiveData.observe(this@UserCenterMoYuFragment) {
                when (it) {
                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.EMPTY -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.EMPTY)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.LOADER_MORE_EMPTY -> {
                        ToastUtil.setText("没有更多内容了")
                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        loginViewModel.checkTokenResultLiveData.observe(this) { tokenBean->
            if (tokenBean != null) {
                if (tokenBean.checkTokenBean.success) {
                    moYuListAdapter.getMyUserId(tokenBean.checkTokenBean.data.id)
                }
            }



            moYuListAdapter.setOnMoYuListItemClickListener(object :
                MoYuListAdapter.OnMoYuListItemClickListener {
                override fun onMoYuListItemClick(id: String) {
                    val intent = Intent(currentActivity, MoYuDetailActivity::class.java)

                    intent.putExtra(Constant.SOB_TOKEN, currentActivity.firsttoken)

                    intent.putExtra(Constant.SOB_MOYU_ID, id)

                    registerForActivityResult.launch(intent)

                }

                override fun onMoYuListImageClick(images: List<String>, position: Int) {
                    val checkImageData = CheckImageData(images, position)

                    val intent = Intent(currentActivity, CheckImageActivity::class.java)

                    intent.putExtra(Constant.SOB_CHECK_IMAGE, checkImageData)

                    startActivity(intent)
                }

                override fun onMoYuListLinkClick(linkUrl: String) {
                    val intent = Intent()

                    intent.data = Uri.parse(linkUrl)

                    startActivity(intent)
                }

                override fun onMoYuUserCenterClick(userId: String) {

                }

                override fun onMoYuUpUpClick(
                    momentId: String,
                    thumbUpList: List<String>?,
                ): Boolean {
                    var canUp = false

                    if (!NetWorkUtils.isConnected(currentActivity)) {
                        //没有网络
                        canUp = false
                    } else {
                        if (thumbUpList.isNullOrEmpty()) {
                            canUp = true
                            currentViewModel.putMoYuMomentUp(tokenBean!!.token, momentId)

                        } else {
                            canUp =
                                if (thumbUpList.contains(tokenBean!!.checkTokenBean.data.id)) {
                                    ToastUtil.setText("已经点赞过了!")
                                    false
                                } else {
                                    if (tokenBean.checkTokenBean.success) {
                                        currentViewModel.putMoYuMomentUp(tokenBean.token,
                                            momentId)

                                        true
                                    } else {
                                        ToastUtil.setText("请先去登陆!")
                                        false
                                    }

                                }
                        }
                    }
                    return canUp
                }

            })

        }
    }

    override fun getMyCurrentActivity() = requireActivity() as UserCenterActivity

    override fun getSuccessView(): View {
        fragmentUserCenterMoYuBinding =
            FragmentUserCenterMoYuBinding.inflate(layoutInflater)

        return fragmentUserCenterMoYuBinding.root
    }

    override fun getCurrentViewModel() = UserCenterMoYuViewModel::class.java

}