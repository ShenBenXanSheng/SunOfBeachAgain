package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.essay.EssayDetailActivity
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.activity.user.UserShouCansActivity
import com.example.sunofbeachagain.adapter.EssayListAdapter
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentEssayListBinding
import com.example.sunofbeachagain.domain.EssayCategoryIdAndNameData
import com.example.sunofbeachagain.repository.EssayListRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.EssayListViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class EssayListFragment(private val essayCategoryIdAndNameData: EssayCategoryIdAndNameData?) :
    BaseFragmentViewModel<HomeActivity, EssayListViewModel>() {

    constructor() : this(null)

    private val TAG = "EssayListFragment"

     lateinit var fragmentEssayListBinding: FragmentEssayListBinding

    private val essayListAdapter by lazy {
        EssayListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private val sobDialog by lazy {
        SobDialog(currentActivity)
    }

    override fun getMyCurrentActivity() = requireActivity() as HomeActivity



    override fun getSuccessView(): View {
        fragmentEssayListBinding = FragmentEssayListBinding.inflate(layoutInflater)
        return fragmentEssayListBinding.root
    }

    override fun initView() {


        setToolBarTheme(true, null, null, null)
        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)

        fragmentEssayListBinding.apply {
            essayListRv.adapter = essayListAdapter.withLoadStateFooter(loadMoreAdapter)
            essaySmartRefresh.setRefreshHeader(MaterialHeader(currentActivity))
        }

    }

    override fun onResume() {
        super.onResume()


    }

    override fun initListener() {
        super.initListener()
        loginViewModel.checkTokenResultLiveData.observe(this) { tokenBean ->


            essayListAdapter.setOnEssayListItemClickListener(object :
                EssayListAdapter.OnEssayListItemClickListener {
                override fun onEssayListItemClick(essayId: String) {
                    val intent = Intent(currentActivity, EssayDetailActivity::class.java)
                    intent.putExtra(Constant.SOB_ESSAY_ID, essayId)

                    if (tokenBean != null) {
                        if (tokenBean.checkTokenBean.success) {
                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                        }
                    }
                    registerForActivityResult.launch(intent)


                }

                override fun onEssayListAvatarClick(userId: String) {
                    val intent = Intent(currentActivity, UserCenterActivity::class.java)
                    intent.putExtra(Constant.SOB_USER_ID, userId)

                    if (tokenBean != null) {

                        intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)

                    }
                    registerForActivityResult.launch(intent)
                }

                override fun onShouCangLongClick(title: String, essayId: String) {
                    sobDialog.setMsgText("确定要收藏吗?", "收藏")

                    sobDialog.setOnDialogShouCangConfirmClick(object :
                        SobDialog.OnDialogShouCangConfirmClick {
                        override fun onDialogShouCangConfirmClick() {
                            val intent = Intent(currentActivity, UserShouCansActivity::class.java)

                            intent.putExtra(Constant.SOB_ESSAY_TITLE, title)

                            intent.putExtra(Constant.SOB_ESSAY_ID,
                                "http://www.debuglive.cn/sob/${essayId}")

                            intent.putExtra(Constant.SOB_IS_SHOUCHANG, true)

                            if (tokenBean != null) {
                                intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                            }

                            registerForActivityResult.launch(intent)
                        }

                    })
                    sobDialog.show()


                }
            })

            fragmentEssayListBinding.essaySmartRefresh.setOnRefreshListener {
                switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                lifecycleScope.launchWhenCreated {
                    essayCategoryIdAndNameData?.let {
                        currentViewModel.getEssayListData(it.id).collectLatest {
                            essayListAdapter.submitData(it)
                        }
                    }
                }
                it.finishRefresh()
            }

        }
    }

    override fun initDataListener() {
        super.initDataListener()
        lifecycleScope.launchWhenCreated {
            essayCategoryIdAndNameData?.let {
                currentViewModel.getEssayListData(it.id).collectLatest {
                    essayListAdapter.submitData(it)
                }
            }
        }

        currentViewModel.essayListRepository.essayListLoadStateLiveData.observe(this) {
            when (it) {
                EssayListRepository.EssayListLoadStatus.LOADING -> {

                }

                EssayListRepository.EssayListLoadStatus.SUCCESS -> {
                    switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                }

                EssayListRepository.EssayListLoadStatus.ERROR -> {
                    switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                }

                EssayListRepository.EssayListLoadStatus.EMPTY -> {
                    ToastUtil.setText("没有更多内容了")

                    lifecycleScope.launchWhenCreated {
                        essayCategoryIdAndNameData?.let {
                            currentViewModel.getEssayListData(it.id).collectLatest {
                                essayListAdapter.submitData(it)
                            }
                        }
                    }

                }
            }
        }
    }



    override fun getCurrentViewModel() = EssayListViewModel::class.java


}