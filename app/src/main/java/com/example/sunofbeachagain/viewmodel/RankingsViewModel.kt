package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.SobRankingData
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class RankingsViewModel : ViewModel() {

    val sobRankingsLiveData = MutableLiveData<List<SobRankingData>>()

    enum class SobRankingsLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val sobRankingsLoadStateLiveData = MutableLiveData<SobRankingsLoadStatus>()

    fun getSobRankingsData() {
        sobRankingsLoadStateLiveData.value = SobRankingsLoadStatus.LOADING
        viewModelScope.launch {
            try {
                val sobRankingsData = RetrofitUtil.userApi.getSobRankingsData()

                if (sobRankingsData.data.isNullOrEmpty()) {
                    sobRankingsLoadStateLiveData.value = SobRankingsLoadStatus.EMPTY
                }

                sobRankingsLoadStateLiveData.value = SobRankingsLoadStatus.SUCCESS
                sobRankingsLiveData.postValue(sobRankingsData.data)
            } catch (e: Exception) {
                sobRankingsLoadStateLiveData.value = SobRankingsLoadStatus.ERROR
                e.printStackTrace()
            }
        }
    }
}