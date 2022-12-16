package com.example.sunofbeachagain.activity.user

import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.adapter.UserCenterViewPageAdapter
import com.example.sunofbeachagain.base.BaseActivityViewModel
import com.example.sunofbeachagain.databinding.ActivityUserCenterBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.viewmodel.UserCenterViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserCenterActivity : BaseActivityViewModel<UserCenterViewModel>() {
    private lateinit var activityUserCenterBinding: ActivityUserCenterBinding

    override fun getSuccessView(): View {
        activityUserCenterBinding = ActivityUserCenterBinding.inflate(layoutInflater)

        return activityUserCenterBinding.root
    }

    private var currentUserId = ""

    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val userCenterTabLayoutTitles = mutableListOf("摸鱼", "文章", "关注", "粉丝")

    private val userCenterViewPageAdapter by lazy {
        UserCenterViewPageAdapter(supportFragmentManager, lifecycle)
    }

    override fun initView() {
        switchDispatchLoadViewState(ActivityLoadViewStatus.SUCCESS)
        intent.getStringExtra(Constant.SOB_USER_ID)?.let {
            currentUserId = it
        }
        setMyTAG(this::class.simpleName)

        setToolBarTheme(true, null, false)

        Log.d(TAG, currentUserId)





        activityUserCenterBinding.apply {
            userCenterViewPageAdapter.getUserId(currentUserId)
            userCenterViewPageAdapter.getTitleData(userCenterTabLayoutTitles)
            userCenterViewpage2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            userCenterViewpage2.adapter = userCenterViewPageAdapter

            tabLayoutMediator = TabLayoutMediator(userCenterTabLayout,
                userCenterViewpage2,
                object : TabLayoutMediator.TabConfigurationStrategy {
                    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                        val textView = TextView(this@UserCenterActivity)
                        val tabTitle = userCenterTabLayoutTitles[position]
                        textView.text = tabTitle
                        textView.setTextColor(ContextCompat.getColor(this@UserCenterActivity,
                            R.color.mainColor))
                        textView.textSize = 16f
                        textView.gravity = Gravity.CENTER
                        tab.customView = textView
                    }
                })
            tabLayoutMediator.attach()

            userCenterToolbar2Container.alpha = 0f
            userCenterContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    val contentViewLayoutParams = userCenterContentView.layoutParams

                    contentViewLayoutParams.height =
                        userCenterContainer.measuredHeight - userCenterToolbar2Container.measuredHeight

                    userCenterContentView.layoutParams = contentViewLayoutParams

                    userCenterNestView.getHeadViewHeight(userCenterHeadView.measuredHeight -
                            userCenterToolbar2Container.measuredHeight)


                    userCenterNestView.getActivityAndDataBinding(this@UserCenterActivity,
                        this@apply)



                    if (userCenterContentView.layoutParams.height != 0) {
                        userCenterContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }

            })


        }
    }

    override fun onResume() {
        super.onResume()
        currentViewModel?.getUserInfo(currentUserId)
    }

    override fun initDataListener() {
        super.initDataListener()
        activityUserCenterBinding.apply {
            loginViewModel.checkTokenResultLiveData.observe(this@UserCenterActivity) { tokenData ->
                if (tokenData != null) {
                    if (tokenData.checkTokenBean.success) {
                        userCenterViewPageAdapter.getToken(tokenData.token)
                        if (currentUserId == tokenData.checkTokenBean.data.id) {
                            userCenterEditProfile.text = "编辑资料"
                        } else {
                            currentViewModel?.getFansStateData( tokenData.token,currentUserId)

                        }


                        userCenterEditProfile.setOnClickListener {
                           val intent = Intent(this@UserCenterActivity,SetUserMsgActivity::class.java)

                            intent.putExtra(Constant.SOB_TOKEN,tokenData.token)

                            intent.putExtra(Constant.SOB_USER_ID,tokenData.checkTokenBean.data.id)

                            startActivity(intent)
                        }
                    }
                }


            }
        }
        activityUserCenterBinding.apply {
            currentViewModel?.apply {
                userInfoDataLiveData.observe(this@UserCenterActivity) {
                    Glide.with(userCenterCover).load(it.cover).into(userCenterCover)

                    val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(150))

                    Glide.with(userCenterAvatar).load(it.avatar).apply(requestOptions)
                        .into(userCenterAvatar)

                    userCenterNickname.text = it.nickname

                    userCenterPosition.text = if (it.position.isNullOrEmpty()) {
                        "刁民"
                    } else {
                        it.position
                    }

                    userCenterMsg.text = if (it.sgin.isNullOrEmpty()) {
                        "学习先进的摸鱼技术中.."
                    } else {
                        it.sgin
                    }

                    userCenterFansCount.text = it.fansCount

                    userCenterFollowCount.text = it.followCount
                }
            }

            currentViewModel?.userFansStateLiveData?.observe(this@UserCenterActivity) {
                userCenterEditProfile.text = when (it.data) {
                    0 -> {
                        "关注"
                    }

                    1 -> {
                        "回粉"
                    }

                    2 -> {
                        "已关注"
                    }
                    3 -> {
                        "互相关注"
                    }

                    else -> {
                        "123"
                    }
                }
            }
        }

    }

    override fun initListener() {
        super.initListener()
        activityUserCenterBinding.apply {
            userCenterToolbar1.setNavigationOnClickListener {
                setTokenToPreActivity()
                finish()
            }

            userCenterToolbar2.setNavigationOnClickListener {
                setTokenToPreActivity()
                finish()
            }
        }
    }

    override fun getCurrentViewModel() = UserCenterViewModel::class.java
}