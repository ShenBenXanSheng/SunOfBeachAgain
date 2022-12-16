package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.base.BaseViewModel
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.domain.bean.QuestionRankingsData
import com.example.sunofbeachagain.repository.QuestionListRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuestionViewModel : BaseViewModel() {
    val questionListRepository = QuestionListRepository()

    val questionListLiveData = MutableLiveData<Flow<PagingData<QuestionData>>>()

    fun getQuestionList(state: String) {
        questionListRepository.getQuestionState(state)

        questionListLiveData.postValue(Pager(PagingConfig(1),
            null) { questionListRepository }.flow.cachedIn(viewModelScope))
    }


    enum class QuestionRankingsLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val questionRankingsLiveData = MutableLiveData<List<QuestionRankingsData>>()

    val questionRankingsLoadStateLiveData = MutableLiveData<QuestionRankingsLoadStatus>()

    fun getQuestionRankingsList() {
        questionRankingsLoadStateLiveData.value = QuestionRankingsLoadStatus.LOADING
        viewModelScope.launch {
            try {
                val questionRankings = RetrofitUtil.questionApi.getQuestionRankings()

                if (questionRankings.data.isEmpty()) {
                    questionRankingsLoadStateLiveData.value = QuestionRankingsLoadStatus.EMPTY
                } else {
                    questionRankingsLoadStateLiveData.value = QuestionRankingsLoadStatus.SUCCESS
                    questionRankingsLiveData.postValue(questionRankings.data)
                }

            } catch (e: Exception) {
                questionRankingsLoadStateLiveData.value = QuestionRankingsLoadStatus.ERROR
                e.printStackTrace()
            }
        }
    }
}


