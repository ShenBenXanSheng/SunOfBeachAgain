package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.base.BaseViewModel
import com.example.sunofbeachagain.domain.bean.MoYuBannerData
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.repository.MoYuListRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MoYuViewModel : BaseViewModel() {

    val moYuBannerLiveData = MutableLiveData<List<MoYuBannerData>>()

    fun getMoYuBanner() {
        viewModelScope.launch {
            try {
                val moYuBanner = RetrofitUtil.moYuApi.getMoYuBanner()
                moYuBannerLiveData.postValue(moYuBanner.data)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    val moYuListRepository: MoYuListRepository = MoYuListRepository()

    val moYuListLiveData = MutableLiveData<Flow<PagingData<MoYuData>>>()



    fun getMoYuList(token: String, state: String) {
        moYuListRepository.getCurrentToken(token)
        moYuListRepository.getCurrentState(state)

        moYuListLiveData.postValue(Pager(PagingConfig(1),
            null,
            pagingSourceFactory = { moYuListRepository })
            .flow.cachedIn(viewModelScope))
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


