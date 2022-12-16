package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.base.BaseViewModel
import com.example.sunofbeachagain.domain.UserInfoMsg
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {


    val userInfoDataLiveData = MutableLiveData<UserInfoMsg>()

    fun getUserInfoData(userId: String) {
        viewModelScope.launch {

            when (userId) {
                "123" -> {
                    val userInfoMsg =
                        UserInfoMsg(
                            R.mipmap.paimeng,
                            "点击头像登陆",
                            "登陆后更好van♂哦",
                            "0",
                            "0",
                            "0",
                            "0", "", "")
                    userInfoDataLiveData.postValue(userInfoMsg)
                }

                "122" -> {
                    val userInfoMsg =
                        UserInfoMsg(
                            R.mipmap.paimeng,
                            "好像没有网络了",
                            "联网后更好van♂哦",
                            "0",
                            "0",
                            "0",
                            "0", "", "")
                    userInfoDataLiveData.postValue(userInfoMsg)
                }

                else -> {
                    val userInfo = RetrofitUtil.userApi.getUserInfo(userId)
                    val userAchievement = RetrofitUtil.userApi.getUserAchievement(userId)


                    val userInfoMsg =
                        UserInfoMsg(
                            userInfo.data.avatar,
                            userInfo.data.nickname,
                            userInfo.data.position,
                            userAchievement.data.momentCount.toString(),
                            userAchievement.data.sob.toString(),
                            userAchievement.data.fansCount.toString(),
                            userAchievement.data.followCount.toString(),
                            if (userInfo.data.cover.isNullOrEmpty()) {
                                R.mipmap.leishen
                            } else {
                                userInfo.data.cover
                            }, userInfo.data.sign)
                    userInfoDataLiveData.postValue(userInfoMsg)
                }
            }

        }
    }

    fun exitLogin(token: String) {
        viewModelScope.launch {
            RetrofitUtil.userApi.exitLogin(token)
        }

    }


}