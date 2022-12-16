package com.example.sunofbeachagain.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.MessageDetailData
import com.example.sunofbeachagain.domain.bean.MessageSystemData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.repository.MessageDetailRepository
import com.example.sunofbeachagain.repository.MessageSystemDetailRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessageDetailViewModel : ViewModel() {

    val messageDetailRepository = MessageDetailRepository()

    val messageDetailDataLiveData = MutableLiveData<Flow<PagingData<MessageDetailData>>>()
    fun getMessageDetailData(token: String, state: String) {
        messageDetailRepository.getToken(token)
        messageDetailRepository.getState(state)
        messageDetailDataLiveData.postValue(Pager(PagingConfig(1),
            null
        ) { messageDetailRepository }.flow.cachedIn(viewModelScope))
    }


    val messageSystemDetailRepository = MessageSystemDetailRepository()

    val messageSystemDetailLiveData = MutableLiveData<Flow<PagingData<MessageSystemData>>>()

    fun getMessageSystemDetailData(token: String) {
        messageSystemDetailRepository.getToken(token)

        messageSystemDetailLiveData.postValue(Pager(PagingConfig(1),
            null) { messageSystemDetailRepository }.flow.cachedIn(viewModelScope))
    }


    val putWenDaMessageRequestLiveData = MutableLiveData<SobBean>()

    val putMoYuMessageRequestLiveData = MutableLiveData<SobBean>()

    val putAiTeMessageRequestLiveData = MutableLiveData<SobBean>()

    fun putWenDaMessage(token: String, msgId: String) {
        viewModelScope.launch {
            try {
                val putWenDaNewMessage = RetrofitUtil.messageApi.putWenDaNewMessage(token, msgId)

                putWenDaMessageRequestLiveData.postValue(putWenDaNewMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun putMoYuMessage(token: String, msgId: String) {
        viewModelScope.launch {
            try {
                val putMoYuNewMessage = RetrofitUtil.messageApi.putMoYuNewMessage(token, msgId)
                putMoYuMessageRequestLiveData.postValue(putMoYuNewMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun putAiTeMessage(token: String, msgId: String) {
        Log.d("TAG",msgId)

        viewModelScope.launch {
            try {
                val putAiTeNewMessage = RetrofitUtil.messageApi.putAiTeNewMessage(token, msgId)
                putAiTeMessageRequestLiveData.postValue(putAiTeNewMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


