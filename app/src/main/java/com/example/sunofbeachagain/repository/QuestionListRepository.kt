package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class QuestionListRepository : PagingSource<Int, QuestionData>() {
    private var questionState: String = ""

    enum class QuestionListLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val questionListLoadState = MutableLiveData<QuestionListLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, QuestionData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuestionData> {
        val currentPage = params.key ?: 1
        if (currentPage == 1){
            questionListLoadState.value = QuestionListLoadStatus.LOADING
        }

        return try {
            val questionList = RetrofitUtil.questionApi.getQuestionList(currentPage, questionState,"-2")

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (questionList.data.list.isEmpty()) null else currentPage + 1
            if (nextPage == null) {
                questionListLoadState.value = QuestionListLoadStatus.EMPTY
            } else {
                questionListLoadState.value = QuestionListLoadStatus.SUCCESS
            }
            LoadResult.Page(questionList.data.list, prePage, nextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            questionListLoadState.value = QuestionListLoadStatus.ERROR
            LoadResult.Page(mutableListOf(), null, null)
        }

    }

    fun getQuestionState(state: String) {


        this.questionState = state
    }
}