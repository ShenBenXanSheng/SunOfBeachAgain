package com.example.sunofbeachagain.fragment

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.activity.order.HomeActivity
import com.example.sunofbeachagain.activity.order.SobRankingsActivity
import com.example.sunofbeachagain.activity.question.QuestionShouCangActivity
import com.example.sunofbeachagain.activity.user.UserCenterActivity
import com.example.sunofbeachagain.activity.user.UserFansOrFollowActivity
import com.example.sunofbeachagain.activity.user.UserMoYuActivity
import com.example.sunofbeachagain.activity.user.UserShouCansActivity
import com.example.sunofbeachagain.base.BaseFragmentViewModel
import com.example.sunofbeachagain.databinding.FragmentUserBinding
import com.example.sunofbeachagain.utils.Constant
import com.example.sunofbeachagain.utils.ToastUtil
import com.example.sunofbeachagain.view.SobDialog
import com.example.sunofbeachagain.viewmodel.QuestionShouCangViewModel
import com.example.sunofbeachagain.viewmodel.UserViewModel


class UserFragment : BaseFragmentViewModel<HomeActivity, UserViewModel>() {

    private lateinit var fragmentUserBinding: FragmentUserBinding


    private val TAG = "UserFragment"
    override fun getSuccessView(): View {
        fragmentUserBinding = FragmentUserBinding.inflate(layoutInflater)
        return fragmentUserBinding.root
    }

    private val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(150))
    private lateinit var sobDialog: SobDialog

    override fun initView() {
        switchDispatchLoadViewState(FragmentLoadViewStatus.SUCCESS)

        setToolBarTheme(false, R.color.mainColor, "我", R.color.white)

        sobDialog = SobDialog(currentActivity)


    }


    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            fragmentUserBinding.apply {
                loginViewModel.checkTokenResultLiveData.observe(this@UserFragment) { checkToken ->
                    if (checkToken != null) {
                        if (checkToken.checkTokenBean.success) {
                            getUserInfoData(checkToken.checkTokenBean.data.id)

                        } else {
                            getUserInfoData("123")
                        }
                    } else {
                        getUserInfoData("122")
                    }


                    userExitContainer.setOnClickListener {
                        sobDialog.apply {
                            if (checkToken != null) {
                                if (checkToken.checkTokenBean.success) {
                                    setMsgText("真的要退出吗?", "退出")
                                    setOnDialogExitConfirmClickListener(object :
                                        SobDialog.OnDialogExitConfirmClickListener {
                                        override fun onDialogExitConfirmClick() {
                                            currentViewModel.exitLogin(checkToken.token)
                                            loginViewModel.checkToken("exit")
                                        }

                                    })
                                } else {
                                    setMsgText("还没登陆", "登陆")
                                    setOnDialogLoginConfirmClickListener(object :
                                        SobDialog.OnDialogLoginConfirmClickListener {
                                        override fun onDialogLoginConfirmClick() {
                                            getTokenAgain()
                                        }

                                    })
                                }
                                show()
                            }


                        }
                    }

                    userAvatar.setOnClickListener {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val intent = Intent(currentActivity, UserCenterActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)
                                intent.putExtra(Constant.SOB_USER_ID,
                                    checkToken.checkTokenBean.data.id)

                                registerForActivityResult.launch(intent)
                                // startActivity(intent)
                            } else {
                                getTokenAgain()
                            }
                        }
                    }

                    userFansContainer.setOnClickListener {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val intent =
                                    Intent(currentActivity, UserFansOrFollowActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                intent.putExtra(Constant.SOB_USER_ID,
                                    checkToken.checkTokenBean.data.id)

                                intent.putExtra(Constant.SOB_FANS_OR_FOLLOW_STATE, "粉丝")
                                registerForActivityResult.launch(intent)
                                // startActivity(intent)
                            } else {
                                getTokenAgain()
                            }
                        }
                    }

                    userFollowContainer.setOnClickListener {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val intent =
                                    Intent(currentActivity, UserFansOrFollowActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                intent.putExtra(Constant.SOB_USER_ID,
                                    checkToken.checkTokenBean.data.id)

                                intent.putExtra(Constant.SOB_FANS_OR_FOLLOW_STATE, "关注")

                                registerForActivityResult.launch(intent)
                                //startActivity(intent)
                            } else {
                                getTokenAgain()
                            }
                        }
                    }

                    userMoyuContainer.setOnClickListener {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val intent = Intent(currentActivity, UserMoYuActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                intent.putExtra(Constant.SOB_USER_ID,
                                    checkToken.checkTokenBean.data.id)

                                registerForActivityResult.launch(intent)

                                //   startActivity(intent)
                            } else {
                                getTokenAgain()
                            }
                        }
                    }

                    userRankingsContainer.setOnClickListener {
                        startActivity(Intent(currentActivity, SobRankingsActivity::class.java))
                    }

                    userShoucangContainer.setOnClickListener {
                        if (checkToken != null) {
                            if (checkToken.checkTokenBean.success) {
                                val intent =
                                    Intent(currentActivity, UserShouCansActivity::class.java)

                                intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                                intent.putExtra(Constant.SOB_IS_SHOUCHANG,false)

                                registerForActivityResult.launch(intent)
                            } else {
                                getTokenAgain()
                            }

                        }
                    }

                    userQuestionShoucangContainer.setOnClickListener {
                        if (checkToken != null) {
                            val intent =
                                Intent(currentActivity, QuestionShouCangActivity::class.java)

                            intent.putExtra(Constant.SOB_TOKEN, checkToken.token)

                            startActivity(intent)
                        }
                    }

                }



                userInfoDataLiveData.observe(this@UserFragment) {
                    userNickname.text = it.nickname
                    userPosition.text = if (it.position.isNullOrEmpty()) {
                        "刁民"
                    } else {
                        it.position
                    }

                    Glide.with(userAvatar).load(it.avatar).apply(requestOptions).into(userAvatar)

                    userMoyuCount.text = it.moyuCount
                    userSobCount.text = it.sobCount
                    userFansCount.text = it.fansCount
                    userFollowCount.text = it.followCount
                }
            }
        }

    }


    override fun getMyCurrentActivity(): HomeActivity {
        return requireActivity() as HomeActivity
    }

    override fun getCurrentViewModel() = UserViewModel::class.java

}