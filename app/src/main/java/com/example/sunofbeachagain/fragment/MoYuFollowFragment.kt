package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.sunofbeachagain.activity.moyu.MoYuDetailActivity
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MoYuListAdapter
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentMoyuFollowBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.repository.MoYuListRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.NetWorkUtils
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.MoYuFollowViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class MoYuFollowFragment : BaseFragmentViewModel<HomeActivity, MoYuFollowViewModel>() {
    private lateinit var fragmentMoyuFollowBinding: FragmentMoyuFollowBinding

    override fun getMyCurrentActivity() = requireActivity() as HomeActivity

    private val TAG = this::class.simpleName

    private val moYuListAdapter by lazy {
        MoYuListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private var myFirstToken = ""

    override fun getSuccessView(): View {
        fragmentMoyuFollowBinding = FragmentMoyuFollowBinding.inflate(layoutInflater)

        return fragmentMoyuFollowBinding.root
    }

    private var moYuFragment: MoYuFragment? = null


    override fun initView() {
        moYuFragment = parentFragment as MoYuFragment

        setToolBarTheme(true, null, null, null)

        moYuListAdapter.getContentLine(3)

        fragmentMoyuFollowBinding.apply {
            moyuFollowRv.adapter = moYuListAdapter.withLoadStateFooter(loadMoreAdapter)
            moyuFollowSmartRefresh.setEnableRefresh(true)
            moyuFollowSmartRefresh.setEnableLoadMore(false)
            moyuFollowSmartRefresh.setRefreshHeader(MaterialHeader(currentActivity))
            moyuFollowSmartRefresh.setEnableHeaderTranslationContent(true)
        }

        myFirstToken = currentActivity.firsttoken

        currentViewModel.getMoYuList(myFirstToken, "follow")

    }


    override fun initListener() {
        super.initListener()


        doubleClickBackTop()
    }

    override fun onResume() {
        super.onResume()
        moYuFragment = parentFragment as MoYuFragment

        doubleClickBackTop()
    }

    override fun onHiddenChanged(hidden: Boolean) {

        moYuFragment = if (hidden){
            null
        }else{
            parentFragment as MoYuFragment
        }

        if (!hidden){
            doubleClickBackTop()
        }
    }

    private fun doubleClickBackTop() {
        moYuFragment?.homeActivity?.setOnHomeDoubleClickListener(object :
            HomeActivity.OnHomeDoubleClickListener {
            override fun onHomeDoubleClick() {
                fragmentMoyuFollowBinding.moyuFollowRv.smoothScrollToPosition(0)
            }

        })
    }

    override fun onPause() {
        super.onPause()
        moYuFragment = null
    }

    override fun emptyReLoad() {
        super.emptyReLoad()
        getTokenAgain()
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@MoYuFollowFragment) { checkToken ->
                if (checkToken != null) {
                    if (checkToken.checkTokenBean.success) {

                        if (checkToken.token != myFirstToken) {
                            myFirstToken = checkToken.token
                            currentViewModel.getMoYuList(checkToken.token, "follow")
                        }

                        moYuListAdapter.getMyUserId(checkToken.checkTokenBean.data.id)
                        moYuListRepository.moYuListLoadStatusLiveData.observe(this@MoYuFollowFragment) {
                            when (it) {
                                MoYuListRepository.MoYuListLoadStatus.LOADING -> {
                                    switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                                }

                                MoYuListRepository.MoYuListLoadStatus.SUCCESS -> {
                                    switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                                }

                                MoYuListRepository.MoYuListLoadStatus.EMPTY -> {
                                    ToastUtil.setText("没有更多数据了")
                                }

                                MoYuListRepository.MoYuListLoadStatus.ERROR -> {
                                    switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                                }
                            }
                        }

                    } else {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.EMPTY)
                        //ToastUtil.setText("登陆已失效，点击重新登陆:)")
                    }

                    fragmentMoyuFollowBinding.moyuFollowSmartRefresh.setOnRefreshListener {

                        if (checkToken.checkTokenBean.success) {
                            getMoYuList(checkToken.token, "follow")

                            it.finishRefresh()
                        } else {
                            switchDispatchLoadViewState(FragmentLoadViewStatus.EMPTY)
                            //ToastUtil.setText("登陆已失效，点击重新登陆:)")
                        }


                    }



                    moYuListAdapter.setOnMoYuListItemClickListener(object :
                        MoYuListAdapter.OnMoYuListItemClickListener {
                        override fun onMoYuListItemClick(id: String) {
                            val intent = Intent(currentActivity, MoYuDetailActivity::class.java)

                            intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                            intent.putExtra(Constant.SOB_MOYU_ID, id)

                            registerForActivityResult.launch(intent)
                            //   startActivity(intent)
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
                            if (checkToken != null) {
                                val intent = Intent(currentActivity, UserCenterActivity::class.java)

                                intent.putExtra(Constant.SOB_USER_ID, userId)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                registerForActivityResult.launch(intent)
                            } else {
                                ToastUtil.setText("网络出错")
                            }
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
                                    currentViewModel.putMoYuMomentUp(checkToken.token, momentId)

                                } else {
                                    canUp =
                                        if (thumbUpList.contains(checkToken.checkTokenBean.data.id)) {
                                            ToastUtil.setText("已经点赞过了!")
                                            false
                                        } else {
                                            if (checkToken.checkTokenBean.success) {
                                                currentViewModel.putMoYuMomentUp(checkToken.token,
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
                } else {
                    switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                }
            }

            currentViewModel.apply {
                moYuListLiveData.observe(this@MoYuFollowFragment) {
                    lifecycleScope.launchWhenCreated {
                        it.collectLatest {
                            moYuListAdapter.submitData(it)
                        }
                    }
                }


            }
        }

    }


    override fun errorReLoad() {
        super.errorReLoad()
        loginViewModel.checkToken(firstToken)
    }

    override fun getCurrentViewModel() = MoYuFollowViewModel::class.java


}