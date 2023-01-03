package com.example.sunofbeachagain.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import com.example.sunofbeachagain.room.SobDataBase

class QuestionListRepository : PagingSource<Int, QuestionData>() {
    private var queryTitle: String = ""
    private lateinit var lifecycleOwner: LifecycleOwner


    private var questionState: String = ""

    enum class QuestionListLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    private val sobDataBase by lazy {
        SobDataBase.getSobDataBase(BaseApp.mContext!!)
    }

    private val questionDao by lazy {
        sobDataBase.getQuestionDao()
    }

    val questionListLoadState = MutableLiveData<QuestionListLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, QuestionData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuestionData> {
        val currentPage = params.key ?: 1
        if (currentPage == 1) {
            questionListLoadState.value = QuestionListLoadStatus.LOADING
        }


        return try {

            val questionList = RetrofitUtil.questionApi.getQuestionList(currentPage,
                questionState,
                "-2").data.list


            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (questionList.isEmpty()) null else currentPage + 1

            if (nextPage == null) {
                questionListLoadState.value = QuestionListLoadStatus.EMPTY
            } else {
                questionListLoadState.value = QuestionListLoadStatus.SUCCESS
            }


            LoadResult.Page(questionList, prePage, nextPage)
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