package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.essay.EssayDetailActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.UserShouCangDetailAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserShouCansDetailBinding
import com.example.sunofbeachagain.repository.UserShouCangDetailRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.UserShouCansDetailViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class UserShouCansDetailActivity : BaseActivityViewModel<UserShouCansDetailViewModel>() {
    private lateinit var activityUserShouCansDetailBinding: ActivityUserShouCansDetailBinding

    override fun getSuccessView(): View {
        activityUserShouCansDetailBinding =
            ActivityUserShouCansDetailBinding.inflate(layoutInflater)
        return activityUserShouCansDetailBinding.root
    }

    private var currentShouCangId = ""

    private val userShouCangDetailAdapter by lazy {
        UserShouCangDetailAdapter()
    }

    private val sobDialog by lazy {
        SobDialog(this)
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    override fun initView() {

        setToolBarTheme(intent.getStringExtra(Constant.SOB_SHOUCANG_NAME).toString(),
            R.mipmap.back,
            R.color.mainColor,
            R.color.white,
            false)

        intent.getStringExtra(Constant.SOB_SHOUCANG_ID)?.let {
            currentShouCangId = it
        }

        activityUserShouCansDetailBinding.apply {
            userShouCangDetailRv.adapter =
                userShouCangDetailAdapter.withLoadStateFooter(loadMoreAdapter)

            userShouCangDetailSmartRefresh.setRefreshHeader(MaterialHeader(this@UserShouCansDetailActivity))
        }
    }

    override fun initListener() {
        super.initListener()

    }

    override fun initDataListener() {
        super.initDataListener()
        activityUserShouCansDetailBinding.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@UserShouCansDetailActivity) { checkToken ->
                if (checkToken != null) {
                    if (checkToken.checkTokenBean.success) {
                        currentViewModel?.getShouCangDetailData(checkToken.token, currentShouCangId)

                        userShouCangDetailSmartRefresh.setOnRefreshListener {
                            currentViewModel?.getShouCangDetailData(checkToken.token,
                                currentShouCangId)
                            it.finishRefresh()
                        }

                        userShouCangDetailAdapter.setOnUserShouCangDetailItemClickListener(object
                            : UserShouCangDetailAdapter.OnUserShouCangDetailItemClickListener {
                            override fun onShouCangDetailItemClick(essayId: String) {
                                val intent = Intent(this@UserShouCansDetailActivity,
                                    EssayDetailActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                intent.putExtra(Constant.SOB_ESSAY_ID, essayId)

                                registerForActivityResult.launch(intent)

                            }

                            override fun onDeleteShouCangLongClick(url: String) {
                                sobDialog.setMsgText("真的要删除收藏吗?", "删除")

                                sobDialog.setOnDialogDeleteShouCangConfirmClick(object :
                                    SobDialog.OnDialogDeleteShouCangConfirmClick {
                                    override fun onDialogDeleteShouCangClick() {
                                        currentViewModel?.checkHasShouCang(checkToken.token, url)
                                    }
                                })

                                sobDialog.show()
                            }

                        })


                        currentViewModel?.apply {
                            userShouCangDetailLiveData.observe(this@UserShouCansDetailActivity) {
                                lifecycleScope.launchWhenCreated {
                                    it.collectLatest {
                                        userShouCangDetailAdapter.submitData(it)
                                    }
                                }
                            }

                            userShouCangDetailRepository.userShouCangDetailLoadState.observe(this@UserShouCansDetailActivity) {
                                when (it) {
                                    UserShouCangDetailRepository.UserShouCangDetailLoadStatus.LOADING -> {
                                        switchDispatchLoadViewState(ActivityLoadViewStatus.LOADING)
                                    }

                                    UserShouCangDetailRepository.UserShouCangDetailLoadStatus.SUCCESS -> {
                                        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
                                    }
                                    UserShouCangDetailRepository.UserShouCangDetailLoadStatus.ERROR -> {
                                        switchDispatchLoadViewState(ActivityLoadViewStatus.ERROR)
                                    }
                                    UserShouCangDetailRepository.UserShouCangDetailLoadStatus.LOAD_MORE_EMPTY -> {
                                        ToastUtil.setText("暂时没有更多了")
                                    }
                                }
                            }

                            checkShouCangLiveData.observe(this@UserShouCansDetailActivity) {
                                if (it.success) {
                                    deleteShouCang(checkToken.token,it.data)
                                }
                            }

                            deleteShouCangLiveData.observe(this@UserShouCansDetailActivity){
                                if (it.success){
                                    currentViewModel?.getShouCangDetailData(checkToken.token, currentShouCangId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun errorReload() {
        super.errorReload()
        currentViewModel?.getShouCangDetailData(firsttoken, currentShouCangId)
    }

    override fun getCurrentViewModel() = UserShouCansDetailViewModel::class.java

}