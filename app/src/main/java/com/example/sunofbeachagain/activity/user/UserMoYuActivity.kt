package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.moyu.MoYuDetailActivity
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MoYuListAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserMoyuBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.repository.UserCenterMoYuRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.UserCenterMoYuViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class UserMoYuActivity : BaseActivityViewModel<UserCenterMoYuViewModel>() {
    private lateinit var activityUserMoyuBinding: ActivityUserMoyuBinding
    override fun getSuccessView(): View {
        activityUserMoyuBinding = ActivityUserMoyuBinding.inflate(layoutInflater)
        return activityUserMoyuBinding.root
    }

    private var currentUserId = ""

    private val moYuListAdapter by lazy {
        MoYuListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    override fun initView() {
        currentUserId = intent.getStringExtra(Constant.SOB_USER_ID).toString()

        setToolBarTheme("摸鱼列表", R.mipmap.back, R.color.mainColor, R.color.white, false)

        currentViewModel?.getUserCenterMoYuData(currentUserId)

        moYuListAdapter.getContentLine(3)
        activityUserMoyuBinding.apply {
            userMoyuRv.adapter = moYuListAdapter.withLoadStateFooter(loadMoreAdapter)

            userMoyuSmartRefresh.setRefreshHeader(MaterialHeader(this@UserMoYuActivity))
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        loginViewModel.checkTokenResultLiveData.observe(this) {
            if (it != null) {
                if (it.checkTokenBean.success) {
                    moYuListAdapter.getMyUserId(it.checkTokenBean.data.id)
                }
            }
        }

        currentViewModel?.apply {
            userCenterMoYuLiveData.observe(this@UserMoYuActivity) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        moYuListAdapter.submitData(it)
                    }
                }
            }

            userCenterMoYuRepository.userCenterMoYuLoadStateLiveData.observe(this@UserMoYuActivity) {
                when (it) {
                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.EMPTY -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.EMPTY)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                    }

                    UserCenterMoYuRepository.UserCenterMoYuLoadStatus.LOADER_MORE_EMPTY -> {
                        ToastUtil.setText("没有更多了")
                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        activityUserMoyuBinding.userMoyuSmartRefresh.setOnRefreshListener {
            currentViewModel?.getUserCenterMoYuData(currentUserId)
            it.finishRefresh()
        }

        loginViewModel.checkTokenResultLiveData.observe(this) { tokenBean ->
            if (tokenBean != null) {
                if (tokenBean.checkTokenBean.success) {
                    moYuListAdapter.getMyUserId(tokenBean.checkTokenBean.data.id)
                }

            }

            moYuListAdapter.setOnMoYuListItemClickListener(object :
                MoYuListAdapter.OnMoYuListItemClickListener {
                override fun onMoYuListItemClick(id: String) {
                    val intent = Intent(this@UserMoYuActivity, MoYuDetailActivity::class.java)

                    intent.putExtra(Constant.SOB_TOKEN, tokenBean?.token)

                    intent.putExtra(Constant.SOB_MOYU_ID, id)

                    registerForActivityResult.launch(intent)
                }

                override fun onMoYuListImageClick(images: List<String>, position: Int) {
                    val checkImageData = CheckImageData(images, position)

                    val intent = Intent(this@UserMoYuActivity, CheckImageActivity::class.java)

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
                    if (tokenBean == null) {
                        //没有网络
                        canUp = false
                    } else {
                        if (!tokenBean.checkTokenBean.success) {
                            canUp = false
                        } else {
                            if (thumbUpList.isNullOrEmpty()) {
                                canUp = true
                                currentViewModel?.putMoYuMomentUp(tokenBean.token, momentId)

                            } else {
                                canUp =
                                    if (thumbUpList.contains(tokenBean.checkTokenBean.data.id)) {
                                        ToastUtil.setText("已经点赞过了!")
                                        false
                                    } else {
                                        if (tokenBean.checkTokenBean.success) {
                                            currentViewModel?.putMoYuMomentUp(tokenBean.token,
                                                momentId)

                                            true
                                        } else {
                                            ToastUtil.setText("请先去登陆!")
                                            false
                                        }

                                    }
                            }
                        }
                    }
                    return canUp
                }

            })

        }
    }

    override fun getCurrentViewModel() = UserCenterMoYuViewModel::class.java
}