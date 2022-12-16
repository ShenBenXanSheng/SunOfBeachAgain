package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.request.RequestInterceptor
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.ForgetPasswordBody
import com.example.sunofbeachagain.domain.body.PhoneCodeBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class ForgetViewModel :ViewModel(){

    val forgetPhoneNumberLiveData = MutableLiveData<SobBean>()

    fun getPhoneCode(phoneCodeBody: PhoneCodeBody){
        viewModelScope.launch {
            try {
                val head = RequestInterceptor.sobCaptchaKey.toString()
                forgetPhoneNumberLiveData.postValue(RetrofitUtil.loginApi.postForgetPhoneNumberCode(head,phoneCodeBody))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val checkPhoneCode = MutableLiveData<SobBean>()

    fun checkPhoneCode(phoneNumber:String,smsCode:String){
        viewModelScope.launch {
            try {
                checkPhoneCode.postValue(RetrofitUtil.loginApi.checkPhoneCodeIsTrue(phoneNumber,smsCode))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    val forgetPasswordLiveData = MutableLiveData<SobBean>()

    fun forgetPassword(smsCode:String,forgetPasswordBody: ForgetPasswordBody){
        viewModelScope.launch {
            try {
                forgetPasswordLiveData.postValue(RetrofitUtil.loginApi.forgetPassword(smsCode, forgetPasswordBody))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}