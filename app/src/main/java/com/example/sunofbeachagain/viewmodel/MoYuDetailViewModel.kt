package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.MoYuComment
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.MoYuCommentBody
import com.example.sunofbeachagain.domain.body.MoYuSubCommentBody
import com.example.sunofbeachagain.repository.MoYuCommentRepository
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import com.example.sunofbeachagain.retrofit.RetrofitUtil.moYuApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class MoYuDetailViewModel : ViewModel() {


    val singleMoYuDataLiveData = MutableLiveData<MoYuData>()

    enum class SingleMoYuDataLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val singleMoYuLoadStateLiveData = MutableLiveData<SingleMoYuDataLoadStatus>()

    fun getSingleMoYuDetail(momentId: String) {
        singleMoYuLoadStateLiveData.value = SingleMoYuDataLoadStatus.LOADING
        viewModelScope.launch {
            try {
                val singleMoYuData = moYuApi.getSingleMoYuData(momentId)

                if (singleMoYuData.success) {
                    singleMoYuLoadStateLiveData.value = SingleMoYuDataLoadStatus.SUCCESS
                    singleMoYuDataLiveData.postValue(singleMoYuData.data)
                } else {
                    singleMoYuLoadStateLiveData.value = SingleMoYuDataLoadStatus.EMPTY
                }

            } catch (e: Exception) {
                e.printStackTrace()
                singleMoYuLoadStateLiveData.value = SingleMoYuDataLoadStatus.ERROR
            }
        }
    }

    val moYuCommentRepository: MoYuCommentRepository = MoYuCommentRepository()

    val moYuCommentDataLiveData = MutableLiveData<Flow<PagingData<MoYuComment>>>()

    fun getMoYuCommentData(momentId: String) {

        moYuCommentRepository.getMomentId(momentId)

        moYuCommentDataLiveData.postValue(Pager(PagingConfig(1),
            pagingSourceFactory = { moYuCommentRepository }).flow.cachedIn(viewModelScope))

    }

    val moYuPostCommentLiveData = MutableLiveData<SobBean>()

    fun postMoYuComment(token: String, moYuCommentBody: MoYuCommentBody) {
        viewModelScope.launch {
            try {
                val postMoYuComment = moYuApi.postMoYuComment(token, moYuCommentBody)
                moYuPostCommentLiveData.postValue(postMoYuComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postMoYuSubComment(token: String,moYuSubCommentBody: MoYuSubCommentBody){
        viewModelScope.launch {
            try {
                moYuApi.postMoYuSubComment(token,moYuSubCommentBody)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
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

