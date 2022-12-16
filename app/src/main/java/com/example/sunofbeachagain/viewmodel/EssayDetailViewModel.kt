package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.bean.UserAchievementBean
import com.example.sunofbeachagain.domain.bean.UserAchievementData
import com.example.sunofbeachagain.domain.body.EssayTippingBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class EssayDetailViewModel : ViewModel() {
    val essayHasUpLiveData = MutableLiveData<SobBean>()

    fun checkHasUp(token: String, essayId: String) {
        viewModelScope.launch {
            try {
                val checkEssayHasUp = RetrofitUtil.essayApi.checkEssayHasUp(token, essayId)
                essayHasUpLiveData.postValue(checkEssayHasUp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val essayUpUpLiveData = MutableLiveData<SobBean>()

    fun putEssayUpUp(token: String, essayId: String) {
        viewModelScope.launch {
            try {
                essayUpUpLiveData.postValue(RetrofitUtil.essayApi.putEssayUpUp(token, essayId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val essayUserAchievement = MutableLiveData<UserAchievementData>()

    fun getAchievement(token:String){
        viewModelScope.launch {
            try {
                essayUserAchievement.postValue(RetrofitUtil.essayApi.getAchievement(token).data)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val essayTippingLiveData = MutableLiveData<SobBean>()

    fun postEssayTipping(token: String,essayTippingBody: EssayTippingBody){
        viewModelScope.launch {
            try {
                val postEssayTipping =
                    RetrofitUtil.essayApi.postEssayTipping(token, essayTippingBody)
                essayTippingLiveData.postValue(postEssayTipping)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}