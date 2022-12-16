package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.bean.UserShouCangDetail
import com.example.sunofbeachagain.repository.UserShouCangDetailRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserShouCansDetailViewModel : ViewModel() {

    val userShouCangDetailRepository = UserShouCangDetailRepository()

    val userShouCangDetailLiveData = MutableLiveData<Flow<PagingData<UserShouCangDetail>>>()

    fun getShouCangDetailData(token: String, shoucangId: String) {
        userShouCangDetailRepository.getToken(token)
        userShouCangDetailRepository.getShouCangId(shoucangId)

        userShouCangDetailLiveData.postValue(Pager(PagingConfig(1),
            initialKey = null) { userShouCangDetailRepository }.flow.cachedIn(viewModelScope))

    }

    val checkShouCangLiveData = MutableLiveData<SobBean>()

    fun checkHasShouCang(token: String,url:String){
        viewModelScope.launch {
            try {
                val checkHasShouCang = RetrofitUtil.userApi.checkHasShouCang(token, url)
                checkShouCangLiveData.postValue(checkHasShouCang)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val deleteShouCangLiveData = MutableLiveData<SobBean>()

    fun deleteShouCang(token: String,id:String){
        viewModelScope.launch {
            try {
                val deleteShouCang = RetrofitUtil.userApi.deleteShouCang(token, id)
                deleteShouCangLiveData.postValue(deleteShouCang)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}