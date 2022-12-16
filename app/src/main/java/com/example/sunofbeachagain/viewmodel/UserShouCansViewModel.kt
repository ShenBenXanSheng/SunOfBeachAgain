package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.ShouCangData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.ShouCangActionBody
import com.example.sunofbeachagain.repository.UserShouCangRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserShouCansViewModel : ViewModel() {

    val userShouCangRepository = UserShouCangRepository()

    val shouCangListLiveData = MutableLiveData<Flow<PagingData<ShouCangData>>>()
    fun getUserShouCangData(token: String) {
        userShouCangRepository.getToken(token)

        shouCangListLiveData.postValue(Pager(PagingConfig(1),
            initialKey = null) { userShouCangRepository }.flow.cachedIn(viewModelScope))

    }

    val postShouCangLiveData = MutableLiveData<SobBean>()

    fun postShouCang(token: String, shouCangActionBody: ShouCangActionBody) {
        viewModelScope.launch {
            try {
                val postAddShouCang =
                    RetrofitUtil.userApi.postAddShouCang(token, shouCangActionBody)

                postShouCangLiveData.postValue(postAddShouCang)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}