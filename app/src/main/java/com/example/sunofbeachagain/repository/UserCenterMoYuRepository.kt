package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class UserCenterMoYuRepository : PagingSource<Int, MoYuData>() {
    private var userId = ""

    enum class UserCenterMoYuLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY, LOADER_MORE_EMPTY
    }

    val userCenterMoYuLoadStateLiveData = MutableLiveData<UserCenterMoYuLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, MoYuData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoYuData> {
        val currentPage = params.key ?: 1

        if (currentPage == 1) {
            userCenterMoYuLoadStateLiveData.value = UserCenterMoYuLoadStatus.LOADING
        }

        return try {
            val userCenterMoYuData = RetrofitUtil.userApi.getUserCenterMoYuData(userId, currentPage)

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (userCenterMoYuData.data.isNullOrEmpty()) null else currentPage + 1

            if (prePage == null && nextPage == null) {
                userCenterMoYuLoadStateLiveData.value = UserCenterMoYuLoadStatus.EMPTY
            } else {
                userCenterMoYuLoadStateLiveData.value = UserCenterMoYuLoadStatus.SUCCESS
            }

            LoadResult.Page(userCenterMoYuData.data, prePage, nextPage)
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
            userCenterMoYuLoadStateLiveData.value =
                UserCenterMoYuLoadStatus.LOADER_MORE_EMPTY

            LoadResult.Page(mutableListOf(), null, null)
        } catch (e: Exception) {
            e.printStackTrace()

            userCenterMoYuLoadStateLiveData.value = UserCenterMoYuLoadStatus.ERROR
            LoadResult.Page(mutableListOf(), null, null)
        }


    }

    fun getUserId(userId: String) {
        this.userId = userId
    }
}