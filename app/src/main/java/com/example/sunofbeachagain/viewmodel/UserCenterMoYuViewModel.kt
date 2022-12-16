package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.repository.UserCenterMoYuRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserCenterMoYuViewModel : ViewModel() {

    val userCenterMoYuRepository by lazy {
        UserCenterMoYuRepository()
    }

    val userCenterMoYuLiveData = MutableLiveData<Flow<PagingData<MoYuData>>>()

    fun getUserCenterMoYuData(userId: String) {
        userCenterMoYuRepository.getUserId(userId)
        userCenterMoYuLiveData.postValue(Pager(PagingConfig(1),
            null) { userCenterMoYuRepository }.flow.cachedIn(viewModelScope))
    }

    fun putMoYuMomentUp(token:String,momentId:String){
        viewModelScope.launch {
            try {
                RetrofitUtil.moYuApi.putMoYuMomentUp(token, momentId)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

    }
}