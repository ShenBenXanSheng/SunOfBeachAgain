package com.example.sunofbeachagain.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.repository.QuestionListRepository
import com.example.sunofbeachagain.room.SobDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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


    lateinit var questionListRepository: QuestionListRepository

    val queryQuestionLiveData = MutableLiveData<Flow<PagingData<QuestionData>>>()

    fun querySingleQuestion(title: String, lifecycleOwner: LifecycleOwner) {


        questionListRepository = QuestionListRepository()

        questionListRepository.getLifecycle(lifecycleOwner)
        questionListRepository.getQuestionDataSources(2)
        questionListRepository.getQueryTitle(title)

        queryQuestionLiveData.value = (Pager(PagingConfig(1),
            initialKey = null) { questionListRepository }.flow.cachedIn(viewModelScope))
    }

    fun queryQuestionList(lifecycleOwner: LifecycleOwner) {
        questionListRepository = QuestionListRepository()

        questionListRepository.getLifecycle(lifecycleOwner)
        questionListRepository.getQuestionDataSources(1)


        queryQuestionLiveData.value = (Pager(PagingConfig(1),
            initialKey = null) { questionListRepository }.flow.cachedIn(viewModelScope))
    }
}