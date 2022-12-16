package com.example.sunofbeachagain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.ShouCangData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class UserShouCangRepository : PagingSource<Int, ShouCangData>() {
    private var token: String = ""

    enum class UserShouCangLoadStatus {
        LOADING, SUCCESS, ERROR, LOAD_EMPTY
    }

    val userShouCangLoadSate = MutableLiveData<UserShouCangLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, ShouCangData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShouCangData> {
        val currentPage = params.key ?: 1
       // userShouCangLoadSate.value = UserShouCangLoadStatus.LOADING
        return try {
            val shouCangListData = RetrofitUtil.userApi.getShouCangListData(token, currentPage)
            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (shouCangListData.data.content.isEmpty()) null else currentPage + 1

            userShouCangLoadSate.value = UserShouCangLoadStatus.SUCCESS

            LoadResult.Page(shouCangListData.data.content, prePage, nextPage)
        } catch (e: java.lang.NullPointerException) {
            userShouCangLoadSate.value = UserShouCangLoadStatus.LOAD_EMPTY
            LoadResult.Error(e)
        } catch (e: Exception) {
            userShouCangLoadSate.value = UserShouCangLoadStatus.ERROR
            LoadResult.Error(e)
        }


    }

    fun getToken(token: String) {
        this.token = token
    }
}