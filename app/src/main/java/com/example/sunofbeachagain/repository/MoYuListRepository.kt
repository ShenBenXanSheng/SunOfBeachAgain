package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class MoYuListRepository : PagingSource<Int, MoYuData>() {
    private var token = ""

    private var state = ""

    enum class MoYuListLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }


    val moYuListLoadStatusLiveData = MutableLiveData<MoYuListLoadStatus>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoYuData> {
        val currentPage = params.key ?: 1
        if (currentPage == 1) {
            moYuListLoadStatusLiveData.value = MoYuListLoadStatus.LOADING
        }

        try {
            val moYuListData = when (state) {
                "recommend" -> {
                    RetrofitUtil.moYuApi.getMoYuListRecommendData(currentPage)
                }

                else -> {
                    RetrofitUtil.moYuApi.getMoYuListFollowData(token, currentPage)
                }
            }


            val prePage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (moYuListData.data.list.isEmpty()) null else currentPage + 1

            moYuListLoadStatusLiveData.value = if (nextPage == null) {

                MoYuListLoadStatus.EMPTY
            } else {

                MoYuListLoadStatus.SUCCESS

            }
            return LoadResult.Page(moYuListData.data.list, prePage, nextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            moYuListLoadStatusLiveData.value = MoYuListLoadStatus.ERROR
            return LoadResult.Page(mutableListOf(), null, null)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, MoYuData>): Int? {
        return null
    }

    fun getCurrentToken(token: String) {
        this.token = token
    }

    fun getCurrentState(state: String) {
        this.state = state
    }

}