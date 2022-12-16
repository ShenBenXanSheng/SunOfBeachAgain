package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.request.RequestInterceptor
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.PhoneCodeBody
import com.example.sunofbeachagain.domain.body.EnrollSobBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class EnrollViewModel : ViewModel() {
    val phoneCodeLiveData = MutableLiveData<SobBean>()
    fun getPhoneCode(phoneCodeBody: PhoneCodeBody) {
        viewModelScope.launch {
            try {
                val head = RequestInterceptor.sobCaptchaKey.toString()
                phoneCodeLiveData.postValue(RetrofitUtil.loginApi.postPhoneNumberCode(head,phoneCodeBody))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val checkPhoneCodeLiveData = MutableLiveData<SobBean>()

    fun checkPhoneCodeIsTrue(phoneNumber:String,smsCode:String){
        viewModelScope.launch {
            try {
                checkPhoneCodeLiveData.postValue(RetrofitUtil.loginApi.checkPhoneCodeIsTrue(phoneNumber,smsCode))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val enrollSobLiveData = MutableLiveData<SobBean>()
    fun enrollSob(smsCode:String,enrollSobBody: EnrollSobBody){
        viewModelScope.launch {
            try {
                enrollSobLiveData.postValue(RetrofitUtil.loginApi.enrollSob(smsCode, enrollSobBody))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}