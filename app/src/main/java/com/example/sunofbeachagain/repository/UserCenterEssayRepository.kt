package com.example.sunofbeachagain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.EssayList
import com.example.sunofbeachagain.retrofit.RetrofitUtil.userApi

class UserCenterEssayRepository : PagingSource<Int, EssayList>() {
    private var userId: String = ""

    enum class UserCenterEssayLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val userCenterEssayLoadStateLiveData = MutableLiveData<UserCenterEssayLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, EssayList>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EssayList> {
        val currentPage = params.key ?: 1

        if (currentPage == 1) {
            userCenterEssayLoadStateLiveData.value = UserCenterEssayLoadStatus.LOADING
        }
        return try {

            val userCenterEssayData = userApi.getUserCenterEssayData(userId, currentPage)

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage =
                if (userCenterEssayData.data.list.isNullOrEmpty()) null else currentPage + 1



            if (prePage == null && nextPage == null){
                userCenterEssayLoadStateLiveData.value = UserCenterEssayLoadStatus.SUCCESS
            }else{
                if (nextPage == null) {
                    userCenterEssayLoadStateLiveData.value = UserCenterEssayLoadStatus.EMPTY
                }
            }



            userCenterEssayLoadStateLiveData.value = UserCenterEssayLoadStatus.SUCCESS


            LoadResult.Page(userCenterEssayData.data.list, prePage, nextPage)
        } catch (e: Exception) {
            e.printStackTrace()

            LoadResult.Page(mutableListOf(), null, null)
        }
    }

    fun getUserId(userId: String) {
        this.userId = userId
    }
}