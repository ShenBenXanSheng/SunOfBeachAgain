package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.UserShouCangDetail
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class UserShouCangDetailRepository : PagingSource<Int, UserShouCangDetail>() {
    private var shouCangId: String = ""
    private var token: String = ""

    enum class UserShouCangDetailLoadStatus {
        LOADING, SUCCESS, ERROR, LOAD_MORE_EMPTY
    }

    val userShouCangDetailLoadState = MutableLiveData<UserShouCangDetailLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, UserShouCangDetail>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserShouCangDetail> {
        val currentPage = params.key ?: 1
    //    userShouCangDetailLoadState.value = UserShouCangDetailLoadStatus.LOADING

        return try {
            val shouCangDetailData =
                RetrofitUtil.userApi.getShouCangDetailData(token, shouCangId, currentPage,"0")

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (shouCangDetailData.data.content.isEmpty()) null else currentPage + 1

            userShouCangDetailLoadState.value = UserShouCangDetailLoadStatus.SUCCESS

            LoadResult.Page(shouCangDetailData.data.content, prePage, nextPage)
        } catch (e: java.lang.NullPointerException) {
            userShouCangDetailLoadState.value = UserShouCangDetailLoadStatus.LOAD_MORE_EMPTY
            LoadResult.Error(e)
        } catch (e: Exception) {
            userShouCangDetailLoadState.value = UserShouCangDetailLoadStatus.ERROR
            LoadResult.Error(e)
        }
    }

    fun getToken(token: String) {
        this.token = token
    }

    fun getShouCangId(shouCangId: String) {
        this.shouCangId = shouCangId
    }
}