package com.example.sunofbeachagain.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.sunofbeachagain.activity.moyu.MoYuDetailActivity
import com.example.sunofbeachagain.activity.moyu.MoYuMomentActivity
import com.example.sunofbeachagain.activity.order.CheckImageActivity
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.adapter.LoadMoreAdapter
import com.example.sunofbeachagain.adapter.MoYuListAdapter
import com.example.sunofbeachagain.adapter.MoYuListBannerAdapter
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentMoyuListBinding
import com.example.sunofbeachagain.domain.CheckImageData
import com.example.sunofbeachagain.repository.MoYuListRepository
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.viewmodel.MoYuViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.flow.collectLatest

class MoYuListFragment : BaseFragmentViewModel<HomeActivity, MoYuViewModel>() {
    private lateinit var fragmentMoyuListBinding: FragmentMoyuListBinding


    private val TAG = this::class.simpleName

    override fun getSuccessView(): View {
        fragmentMoyuListBinding = FragmentMoyuListBinding.inflate(layoutInflater)

        return fragmentMoyuListBinding.root
    }

    private lateinit var moYuListBannerAdapter: MoYuListBannerAdapter

    private val moYuListAdapter by lazy {
        MoYuListAdapter()
    }

    private val loadMoreAdapter by lazy {
        LoadMoreAdapter()
    }

    private val upSp = BaseApp.mContext?.getSharedPreferences("UpSp", Context.MODE_PRIVATE)

    private var moYuFragment: MoYuFragment? = null

    override fun initView() {

        setToolBarTheme(true, null, null, null)

        moYuListAdapter.getContentLine(3)


        loginViewModel.checkToken(firstToken)

        currentViewModel.apply {
            getMoYuBanner()

            getMoYuList("null", "recommend")
        }

        fragmentMoyuListBinding.apply {
            moyuListRv.adapter = moYuListAdapter.withLoadStateFooter(loadMoreAdapter)


            moyuListContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    moyuNv.getHeadViewHeight(moyuHeadView.measuredHeight)

                    val layoutParams = moyuListRv.layoutParams
                    layoutParams.height = moyuListContainer.measuredHeight
                    moyuListRv.layoutParams = layoutParams



                    if (moyuListRv.layoutParams.height != 0) {
                        moyuListContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }

            })

            moyuSmartRefresh.setEnableRefresh(true)
            moyuSmartRefresh.setEnableLoadMore(false)
            moyuSmartRefresh.setRefreshHeader(MaterialHeader(currentActivity))
            moyuSmartRefresh.setEnableHeaderTranslationContent(true)
            moyuNv.getSmartRefresh(fragmentMoyuListBinding)
        }


    }

    override fun initListener() {
        super.initListener()


        fragmentMoyuListBinding.apply {
            moyuListVp.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {

                }

                override fun onPageSelected(position: Int) {
                    selectPoint(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })

            moyuSmartRefresh.setOnRefreshListener {
                currentViewModel.getMoYuList("null", "recommend")
                upSp?.edit()?.clear()?.apply()
                it.finishRefresh()
            }
        }


    }

    private fun selectPoint(position: Int) {
        fragmentMoyuListBinding.apply {
            moyuPoint1.isSelected = false
            moyuPoint2.isSelected = false
            moyuPoint3.isSelected = false
            moyuPoint4.isSelected = false
            moyuPoint5.isSelected = false
            when (position % 5) {
                0 -> {
                    moyuPoint1.isSelected = true
                }

                1 -> {
                    moyuPoint2.isSelected = true
                }

                2 -> {
                    moyuPoint3.isSelected = true
                }

                3 -> {
                    moyuPoint4.isSelected = true
                }

                4 -> {
                    moyuPoint5.isSelected = true
                }
            }
        }

    }


    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            moYuBannerLiveData.observe(this@MoYuListFragment) {
                moYuListBannerAdapter = MoYuListBannerAdapter(it)

                fragmentMoyuListBinding.apply {
                    moyuListVp.adapter = moYuListBannerAdapter

                    moyuListVp.currentItem = 500
                    moyuListVp.offscreenPageLimit = 3

                    moYuListBannerAdapter.setOnMoYuBannerClickListener(object :
                        MoYuListBannerAdapter.OnMoYuBannerClickListener {
                        override fun onMoYuBannerClick(url: String) {
                            val intent = Intent()

                            intent.data = Uri.parse(url)

                            startActivity(intent)
                        }

                    })
                }
            }



            moYuListLiveData.observe(this@MoYuListFragment) {
                lifecycleScope.launchWhenCreated {
                    it.collectLatest {
                        moYuListAdapter.submitData(it)
                    }
                }
            }

            moYuListRepository.moYuListLoadStatusLiveData.observe(this@MoYuListFragment) {
                when (it) {

                    MoYuListRepository.MoYuListLoadStatus.LOADING -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.LOADING)
                    }

                    MoYuListRepository.MoYuListLoadStatus.SUCCESS -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)
                    }

                    MoYuListRepository.MoYuListLoadStatus.EMPTY -> {
                        ToastUtil.setText("好像没有更多内容了")
                    }

                    MoYuListRepository.MoYuListLoadStatus.ERROR -> {
                        switchDispatchLoadViewState(FragmentLoadViewStatus.ERROR)
                    }
                }
            }




            loginViewModel.checkTokenResultLiveData.observe(this@MoYuListFragment) { tokenBean ->
                if (tokenBean != null) {
                    if (tokenBean.checkTokenBean.success) {
                        moYuListAdapter.getMyUserId(tokenBean.checkTokenBean.data.id)
                    }

                }

                fragmentMoyuListBinding.moyuPublicationBt.setOnClickListener {
                    if (tokenBean != null) {
                        if (tokenBean.checkTokenBean.success) {
                            val intent = Intent(currentActivity, MoYuMomentActivity::class.java)
                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)
                            registerForActivityResult.launch(intent)
                        } else {
                            getTokenAgain()
                        }
                    }
                }
                moYuListAdapter.setOnMoYuListItemClickListener(object :
                    MoYuListAdapter.OnMoYuListItemClickListener {
                    override fun onMoYuListItemClick(id: String) {
                        val intent = Intent(currentActivity, MoYuDetailActivity::class.java)

                        intent.putExtra(Constant.SOB_TOKEN, tokenBean?.token)

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
                        if (tokenBean != null) {
                            val intent = Intent(currentActivity, UserCenterActivity::class.java)

                            intent.putExtra(Constant.SOB_USER_ID, userId)

                            intent.putExtra(Constant.SOB_TOKEN, tokenBean.token)

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
                        if (tokenBean == null) {
                            //没有网络
                            canUp = false
                        } else {
                            if (!tokenBean.checkTokenBean.success) {
                                canUp = false
                            } else {
                                if (thumbUpList.isNullOrEmpty()) {
                                    canUp = true
                                    currentViewModel.putMoYuMomentUp(tokenBean.token, momentId)

                                } else {
                                    canUp =
                                        if (thumbUpList.contains(tokenBean.checkTokenBean.data.id)) {
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
                        }


                        return canUp
                    }
                })
            }


        }
    }

    override fun onResume() {
        super.onResume()
        moYuFragment = parentFragment as MoYuFragment

        doubleClickBackTop()
    }

    override fun onHiddenChanged(hidden: Boolean) {

        moYuFragment = if (hidden) {
            null
        } else {
            parentFragment as MoYuFragment

        }

        if (!hidden) {
            doubleClickBackTop()
        }
    }

    private fun doubleClickBackTop() {
        moYuFragment?.homeActivity?.setOnHomeDoubleClickListener(object :
            HomeActivity.OnHomeDoubleClickListener {
            override fun onHomeDoubleClick() {
                fragmentMoyuListBinding.moyuListRv.smoothScrollToPosition(0)
            }

        })
    }


    override fun onPause() {
        super.onPause()

        moYuFragment = null
    }

    override fun errorReLoad() {
        super.errorReLoad()
        currentViewModel.apply {
            getMoYuBanner()

            getMoYuList("null", "recommend")
        }
    }


    override fun getCurrentViewModel() = MoYuViewModel::class.java

    override fun getMyCurrentActivity() = requireActivity() as HomeActivity


}