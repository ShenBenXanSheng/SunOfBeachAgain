package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.MoYuMomentBean
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.bean.TopicData
import com.example.sunofbeachagain.domain.body.MoYuMomentBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MoYuMomentViewModel : ViewModel() {
    val topicLiveData = MutableLiveData<List<TopicData>>()

    fun getTopicData() {
        viewModelScope.launch {
            try {
                val topicData = RetrofitUtil.moYuApi.getTopicData()
                topicLiveData.postValue(topicData.data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val postImageLiveData = MutableLiveData<SobBean>()

    fun postImage(token: String, part: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val postMoYuImage = RetrofitUtil.moYuApi.postMoYuImage(token, part)
                postImageLiveData.postValue(postMoYuImage)
            } catch (e: Exception) {

            }
        }
    }

    val postMoYuMomentLiveData = MutableLiveData<MoYuMomentBean>()

    fun postMoYuMoment(token: String, moYuMomentBody: MoYuMomentBody) {
        viewModelScope.launch {
            try {
                val postMoYuMoment = RetrofitUtil.moYuApi.postMoYuMoment(token, moYuMomentBody)

                postMoYuMomentLiveData.postValue(postMoYuMoment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}