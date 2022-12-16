package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.essay.EssayDetailActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.activity.user.UserShouCansActivity
import com.example.sunofbeachagain.adapter.EssayListAdapter
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentUserCenterEssayBinding
import com.example.sunofbeachagain.repository.UserCenterEssayRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.UserCenterEssayViewModel
import kotlinx.coroutines.flow.collectLatest

class UserCenterEssayFragment(val userId: String?, val token: String?) :
    BaseFragmentViewModel<UserCenterActivity, UserCenterEssayViewModel>() {


    private lateinit var fragmentUserCenterEssayBinding: FragmentUserCenterEssayBinding

    override fun getMyCurrentActivity() = requireActivity() as UserCenterActivity

    override fun getSuccessView(): View {
        fragmentUserCenterEssayBinding = FragmentUserCenterEssayBinding.inflate(layoutInflater)

        return fragmentUserCenterEssayBinding.root
    }

    private var currentUserId = ""

    private var currentToken = ""

    private val essayLisAdapter by lazy {
        EssayListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private val sobDialog by lazy {
        SobDialog(currentActivity)
    }

    override fun initView() {
        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
        setToolBarTheme(true, null, null, null)

        userId?.let {
            currentUserId = it
        }

        token?.let {
            currentToken = it
        }

        currentViewModel.getUserCenterEssayData(currentUserId)

        fragmentUserCenterEssayBinding.userCenterEssayRv.adapter =
            essayLisAdapter.withLoadStateFooter(loadMoreAdapter)
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            userCenterEssayLiveData.observe(this@UserCenterEssayFragment) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        essayLisAdapter.submitData(it)
                    }
                }
            }

            userCenterEssayRepository.userCenterEssayLoadStateLiveData.observe(this@UserCenterEssayFragment) {
                when (it) {
                    UserCenterEssayRepository.UserCenterEssayLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    UserCenterEssayRepository.UserCenterEssayLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    UserCenterEssayRepository.UserCenterEssayLoadStatus.EMPTY -> {
                        ToastUtil.setText("没有更多内容了")
                    }

                    UserCenterEssayRepository.UserCenterEssayLoadStatus.ERROR -> {

                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        loginViewModel.checkTokenResultLiveData.observe(this) { tokenBean ->
            essayLisAdapter.setOnEssayListItemClickListener(object :
                EssayListAdapter.OnEssayListItemClickListener {
                override fun onEssayListItemClick(essayId: String) {
                    val intent = Intent(currentActivity, EssayDetailActivity::class.java)
                    intent.putExtra(Constant.SOB_ESSAY_ID, essayId)
                    if (tokenBean != null) {
                        if (tokenBean.checkTokenBean.success) {
                            intent.putExtra(Constant.SOB_TOKEN,tokenBean.token)
                        }
                    }
                    //registerForActivityResult.launch(intent)
                    startActivity(intent)
                }

                override fun onEssayListAvatarClick(userId: String) {

                }

                override fun onShouCangLongClick(title: String, essayId: String) {
                    sobDialog.setMsgText("确定要收藏吗?", "收藏")
                    sobDialog.setOnDialogShouCangConfirmClick(object :SobDialog.OnDialogShouCangConfirmClick{
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
        }

    }

    override fun getCurrentViewModel() = UserCenterEssayViewModel::class.java


}