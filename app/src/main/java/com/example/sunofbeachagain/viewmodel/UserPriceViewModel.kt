package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.domain.bean.UserPriceBean
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class UserPriceViewModel :ViewModel(){

    val userPriceLiveData = MutableLiveData<UserPriceBean>()

    fun getUserPrice(){
        viewModelScope.launch {
            try {
                val userPrice = RetrofitUtil.userPriceApi.getUserPrice()

                userPriceLiveData.postValue(userPrice)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}