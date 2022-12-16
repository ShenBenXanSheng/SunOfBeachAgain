package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.domain.UserInfoMsg
import com.example.sunofbeachagain.domain.bean.UserFansStateBean
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class UserCenterViewModel : ViewModel() {
    val userInfoDataLiveData = MutableLiveData<UserInfoMsg>()

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
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
                        },
                        userInfo.data.sign)

                userInfoDataLiveData.postValue(userInfoMsg)
            } catch (e: Exception) {
                e.printStackTrace()
                val userInfoMsg =
                    UserInfoMsg(
                        R.mipmap.paimeng,
                        "网络可能出错",
                        "刷新一下吧",
                        "0",
                        "0",
                        "0",
                        "0", "", "啊吧啊吧")
                userInfoDataLiveData.postValue(userInfoMsg)
            }

        }

    }

    val userFansStateLiveData = MutableLiveData<UserFansStateBean>()

    fun getFansStateData(token: String, userId: String) {
        viewModelScope.launch {
            try {
                val userFansState = RetrofitUtil.userApi.getUserFansState(token, userId)
                userFansStateLiveData.postValue(userFansState)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}