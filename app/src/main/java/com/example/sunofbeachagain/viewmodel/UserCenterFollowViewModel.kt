package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.bean.UserFollowAndFans
import com.example.sunofbeachagain.repository.UserFollowAndFansRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserCenterFollowViewModel : ViewModel() {

    val userFollowAndFansRepository by lazy {
        UserFollowAndFansRepository()
    }

    fun getUserFollowOrData(
        token: String,
        userId: String,
        state: String,
    ): Flow<PagingData<UserFollowAndFans>> {
        userFollowAndFansRepository.getToken(token)
        userFollowAndFansRepository.getUserId(userId)
        userFollowAndFansRepository.getLoadState(state)

        return Pager(PagingConfig(1), null) { userFollowAndFansRepository }.flow.cachedIn(
            viewModelScope)
    }

    val followLiveData = MutableLiveData<SobBean>()

    fun followUser(token: String,userId: String){
        viewModelScope.launch {
            try {
                followLiveData.postValue(RetrofitUtil.userApi.postFollowUser(token, userId))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun unfollowUser(token: String,userId: String){
        viewModelScope.launch {
            try {
                followLiveData.postValue(RetrofitUtil.userApi.postUnfollowUser(token, userId))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}