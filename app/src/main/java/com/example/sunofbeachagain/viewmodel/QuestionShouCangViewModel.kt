package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.room.QuestionEntity
import com.example.sunofbeachagain.room.SobDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionShouCangViewModel : ViewModel() {

    private val sobDataBase by lazy {
        SobDataBase.getSobDataBase(BaseApp.mContext!!)
    }

    private val questionDao by lazy {
        sobDataBase.getQuestionDao()
    }

    fun deleteQuestion(wendaId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionDao.deleteQuestionData(wendaId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearQuestion() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionDao.clearQuestionList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    val questionEntitySearchLiveData = MutableLiveData<List<QuestionEntity>>()


    fun querySearchQuestion(title: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionEntitySearchLiveData.postValue(questionDao.queryQuestionData(title))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun queryQuestionList() = questionDao.queryQuestionList()


}