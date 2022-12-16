package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.CreateShouCangBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil.userApi
import com.example.sunofbeachagain.utils.ToastUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserCreateShouCangViewModel : ViewModel() {
    val shouCangCoverLiveData = MutableLiveData<SobBean>()

    fun postShouCangCover(token: String, part: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val postShouCangCover = userApi.postShouCangCover(token, part)
                shouCangCoverLiveData.postValue(postShouCangCover)
            } catch (e: Exception) {
                ToastUtil.setText("服务器异常")
                e.printStackTrace()

            }
        }
    }

    val createShouCangLiveData = MutableLiveData<SobBean>()

    fun postCreateShouCang(token: String, createShouCangBody: CreateShouCangBody) {
        viewModelScope.launch {
            try {
                createShouCangLiveData.postValue(userApi.postCreateShouCang(token, createShouCangBody))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}