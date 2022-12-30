package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.domain.bean.QuestionData
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import com.example.sunofbeachagain.room.QuestionEntity
import com.example.sunofbeachagain.room.SobDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionListRepository : PagingSource<Int, QuestionData>() {
    private  var queryTitle: String = ""
    private lateinit var lifecycleOwner: LifecycleOwner


    private var sources: Int = 0

    //sources:判断从哪里获取数据
    //0-->网络
    //1-->数据库全部查询
    //2-->数据库单挑查询
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



            val questionList: List<QuestionData> = when (sources) {
                0 -> {
                    RetrofitUtil.questionApi.getQuestionList(currentPage,
                        questionState,
                        "-2").data.list
                }

                1 -> {
                    val list = mutableListOf<QuestionData>()
                   questionDao.queryQuestionList().observe(lifecycleOwner){questionEntityList->

                       questionEntityList.reversed().forEach {
                           val questionData = QuestionData(it.answerCount,
                               it.avatar,
                               it.createTime,
                               it.wendaId,
                               it.nickname,
                               it.sob,it.thumbUp,it.title,it.userId,it.viewCount)
                           list.add(questionData)
                       }

                   }


                    list
                }


                2 -> {
                    val list = mutableListOf<QuestionData>()
                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

                        questionDao.queryQuestionData(queryTitle).forEach {

                            val questionData = QuestionData(it.answerCount,
                                it.avatar,
                                it.createTime,
                                it.wendaId,
                                it.nickname,
                                it.sob,it.thumbUp,it.title,it.userId,it.viewCount)
                            list.add(questionData)

                        }

                    }


                    list
                }
                else -> {
                    mutableListOf<QuestionData>()
                }
            }



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

    //必须调用
    fun getQuestionDataSources(sources: Int) {
        this.sources = sources
    }

    //网络调用
    fun getQuestionState(state: String) {
        this.questionState = state
    }

   //数据库调用
    fun getLifecycle(lifecycleOwner: LifecycleOwner){
        this.lifecycleOwner = lifecycleOwner
    }

    //数据库单词查询调用
    fun getQueryTitle(title:String){
        this.queryTitle = title
    }
}