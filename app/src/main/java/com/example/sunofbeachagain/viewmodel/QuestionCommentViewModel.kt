package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.QuestionCommentBody
import com.example.sunofbeachagain.domain.body.QuestionSubCommentBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class QuestionCommentViewModel :ViewModel(){

    val questionCommentLiveData = MutableLiveData<SobBean>()


    fun postQuestionComment(token:String,questionCommentBody: QuestionCommentBody){
        viewModelScope.launch {
            try {
                questionCommentLiveData.postValue(RetrofitUtil.questionApi.postQuestionComment(token, questionCommentBody))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun postQuestionSubComment(token: String,questionSubCommentBody: QuestionSubCommentBody){
        viewModelScope.launch {
            try {
                val postQuestionSubComment =
                    RetrofitUtil.questionApi.postQuestionSubComment(token, questionSubCommentBody)

                questionCommentLiveData.postValue(postQuestionSubComment)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}