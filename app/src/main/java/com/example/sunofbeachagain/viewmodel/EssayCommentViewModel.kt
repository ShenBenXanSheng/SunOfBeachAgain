package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.EssayCommentBody
import com.example.sunofbeachagain.domain.body.EssaySubCommentBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class EssayCommentViewModel :ViewModel(){
    val postEssayCommentLiveData = MutableLiveData<SobBean>()

    fun postEssayComment(token:String,essayCommentBody: EssayCommentBody){
        viewModelScope.launch {
            try {
                val postEssayComment =
                    RetrofitUtil.essayApi.postEssayComment(token, essayCommentBody)
                postEssayCommentLiveData.postValue(postEssayComment)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


    fun postEssaySubComment(token: String,essaySubCommentBody: EssaySubCommentBody){
        viewModelScope.launch {
            try {
                postEssayCommentLiveData.postValue(RetrofitUtil.essayApi.postEssaySubComment(token, essaySubCommentBody))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}