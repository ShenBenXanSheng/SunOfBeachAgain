package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.EssayList
import com.example.sunofbeachagain.repository.UserCenterEssayRepository
import kotlinx.coroutines.flow.Flow

class UserCenterEssayViewModel : ViewModel() {

    val userCenterEssayRepository by lazy {
        UserCenterEssayRepository()
    }

    val userCenterEssayLiveData = MutableLiveData<Flow<PagingData<EssayList>>>()

    fun getUserCenterEssayData(userId: String) {
        userCenterEssayRepository.getUserId(userId)

        userCenterEssayLiveData.postValue(Pager(PagingConfig(1),
            null) { userCenterEssayRepository }.flow.cachedIn(viewModelScope))
    }
}