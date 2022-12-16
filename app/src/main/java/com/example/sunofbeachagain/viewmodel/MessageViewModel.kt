package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.base.BaseViewModel
import com.example.sunofbeachagain.domain.bean.MessageCountData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class MessageViewModel :BaseViewModel(){
    val messageCountLiveData = MutableLiveData<MessageCountData>()

    fun getMessageCount(token:String){
        viewModelScope.launch {
            try {
                val messageCount = RetrofitUtil.messageApi.getMessageCount(token)
                messageCountLiveData.postValue(messageCount.data)

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val messageReadAllLiveData = MutableLiveData<SobBean>()

    fun readAllMessage(token:String){
        viewModelScope.launch {
            try {
                messageReadAllLiveData.postValue(RetrofitUtil.messageApi.readAllMessage(token))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}