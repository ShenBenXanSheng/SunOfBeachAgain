package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.UserFollowAndFans
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class UserFollowAndFansRepository : PagingSource<Int, UserFollowAndFans>() {
    private var token = ""
    private var state = ""
    private var userId = ""

    enum class UserFollowAndFansLoadStatus {
        LOADING, SUCCESS, LOAD_MORE_EMPTY, ERROR,EMPTY
    }

    val userFollowAndFansLoadState = MutableLiveData<UserFollowAndFansLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, UserFollowAndFans>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserFollowAndFans> {
        val currentPage = params.key ?: 1

        if (currentPage == 1) {
            userFollowAndFansLoadState.value = UserFollowAndFansLoadStatus.LOADING
        }



        return try {
            val followOrFansList = when (state) {
                "关注" -> {
                    val userFollowData =
                        RetrofitUtil.userApi.getUserFollowData(token, userId, currentPage)



                    userFollowData.data.list


                }

                else -> {
                    val userFansData =
                        RetrofitUtil.userApi.getUserFansData(token, userId, currentPage)
                    userFansData.data.list
                }
            }


            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (followOrFansList.isNullOrEmpty()) null else currentPage + 1

            if (prePage==null && nextPage == null){
                userFollowAndFansLoadState.value = UserFollowAndFansLoadStatus.EMPTY
            }else{
                if (nextPage == null) {
                    userFollowAndFansLoadState.value = UserFollowAndFansLoadStatus.LOAD_MORE_EMPTY
                }

                userFollowAndFansLoadState.value = UserFollowAndFansLoadStatus.SUCCESS
            }



            LoadResult.Page(followOrFansList, prePage, nextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            userFollowAndFansLoadState.value = UserFollowAndFansLoadStatus.ERROR
            LoadResult.Page(mutableListOf(), null, null)
        }


    }

    fun getUserId(userId: String) {
        this.userId = userId
    }

    fun getLoadState(state: String) {

        this.state = state
    }

    fun getToken(token: String) {
        this.token = token
    }
}