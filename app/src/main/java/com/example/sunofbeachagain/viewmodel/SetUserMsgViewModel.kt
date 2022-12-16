package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.DetailedUserInfoData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.ChangeUserInfoBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SetUserMsgViewModel : ViewModel() {
    val userInfoLiveData = MutableLiveData<DetailedUserInfoData>()
    fun getUserInfo(token: String) {
        viewModelScope.launch {
            try {
                val userInfo = RetrofitUtil.userApi.getDetailedUserInfo(token)

                userInfoLiveData.postValue(userInfo.data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    val userNameCanUserLiveData = MutableLiveData<SobBean>()

    fun checkUserNameCanUser(userName: String){
        viewModelScope.launch {
            try {
                val checkNicknameCanUser = RetrofitUtil.userApi.checkNicknameCanUser(userName)
                userNameCanUserLiveData.postValue(checkNicknameCanUser)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val changeUserSexAndNickNameLiveData = MutableLiveData<SobBean>()

    fun changeUserSexAndName(token: String, sex: String, userName: String) {
        viewModelScope.launch {
            try {
                changeUserSexAndNickNameLiveData.postValue(RetrofitUtil.userApi.changeUserNicknameAndSex(token, sex, userName))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val changeUserInfoResultLiveData = MutableLiveData<SobBean>()

    fun changeUserInfo(token:String,changeUserInfoBody: ChangeUserInfoBody){
        viewModelScope.launch {
            try {
                val changeUserInfo = RetrofitUtil.userApi.changeUserInfo(token, changeUserInfoBody)
                changeUserInfoResultLiveData.postValue(changeUserInfo)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val postUserAvatarLiveData = MutableLiveData<SobBean>()
    fun postUserAvatar(token:String,multipart: MultipartBody.Part){
        viewModelScope.launch {
            try {
                val postAvatarImage = RetrofitUtil.userApi.postAvatarImage(token, multipart)
                postUserAvatarLiveData.postValue(postAvatarImage)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


    val changeUserAvatarLiveData = MutableLiveData<SobBean>()
    fun changeUserAvatar(token: String,avatar:String){
        viewModelScope.launch {
            try {
                changeUserAvatarLiveData.postValue(RetrofitUtil.userApi.putUserAvatar(token, avatar))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


}