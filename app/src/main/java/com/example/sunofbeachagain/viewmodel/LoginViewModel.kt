package com.example.sunofbeachagain.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.request.RequestInterceptor
import com.example.sunofbeachagain.domain.CheckTokenDataHasToken
import com.example.sunofbeachagain.domain.bean.CheckTokenBean
import com.example.sunofbeachagain.domain.bean.CheckTokenData
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.UserBody
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val loginAvatarLiveData = MutableLiveData<String>()

    val tokenLiveData = MutableLiveData<String>()

    val loginResultLiveData = MutableLiveData<SobBean>()

    val checkTokenResultLiveData = MutableLiveData<CheckTokenDataHasToken?>()

    fun getLoginAvatar(phoneNumber: String) {
        viewModelScope.launch {
            try {
                val userAvatarByPhoneNumber =
                    RetrofitUtil.loginApi.getUserAvatarByPhoneNumber(phoneNumber)
                loginAvatarLiveData.postValue(userAvatarByPhoneNumber.data)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun postLogin(userBody: UserBody, verifyCode: String) {
        val head = RequestInterceptor.sobCaptchaKey.toString()
        val postLogin = RetrofitUtil.loginApi.postLogin(head, verifyCode, userBody)

        postLogin.enqueue(object : Callback<SobBean> {
            override fun onResponse(call: Call<SobBean>, response: Response<SobBean>) {
                val token = response.headers()["sob_token"]
                tokenLiveData.postValue(token)

                loginResultLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<SobBean>, t: Throwable) {


            }

        })
    }

    fun checkToken(token: String) {
        viewModelScope.launch {
            try {
                val checkTokenData = RetrofitUtil.loginApi.checkToken(token)

                val checkTokenDataHasToken = CheckTokenDataHasToken(token,checkTokenData)
                checkTokenResultLiveData.postValue(checkTokenDataHasToken)
            } catch (e: Exception) {
                checkTokenResultLiveData.value = null

                e.printStackTrace()
            }
        }
    }
}