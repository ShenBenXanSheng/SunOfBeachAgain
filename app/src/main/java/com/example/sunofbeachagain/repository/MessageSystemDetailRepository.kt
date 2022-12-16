package com.example.sunofbeachagain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.MessageSystemData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class MessageSystemDetailRepository : PagingSource<Int, MessageSystemData>() {
    override fun getRefreshKey(state: PagingState<Int, MessageSystemData>): Int? {
        return null
    }

    private var token: String = ""
    enum class MessageSystemLoadStatus{
        LOADING,SUCCESS,ERROR,EMPTY
    }
    val messageSystemDetailLoadStateLiveData =
        MutableLiveData<MessageSystemLoadStatus>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageSystemData> {
        val currentPage = params.key ?: 1

        if (currentPage == 1) {
            messageSystemDetailLoadStateLiveData.value =
                MessageSystemLoadStatus.LOADING
        }

        return try {
            val messageSystemData = RetrofitUtil.messageApi.getMessageSystemData(token, currentPage)

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (messageSystemData.data.content.isEmpty()) null else currentPage + 1


            messageSystemDetailLoadStateLiveData.value = if (nextPage == null) {
                MessageSystemLoadStatus.EMPTY
            } else {
                MessageSystemLoadStatus.SUCCESS
            }

            LoadResult.Page(messageSystemData.data.content, prePage, nextPage)
        } catch (e: Exception) {
            messageSystemDetailLoadStateLiveData.value =
                MessageSystemLoadStatus.ERROR

            LoadResult.Page(mutableListOf(), null, null)
        }

    }

    fun getToken(token: String) {
        this.token = token
    }

}