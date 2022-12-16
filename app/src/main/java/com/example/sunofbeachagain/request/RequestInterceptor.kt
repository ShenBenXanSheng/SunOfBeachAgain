package com.colin.request

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
Date:2022-02-24
Time:16:18
author:colin
 * 请求拦截器，拦截登陆时候验证码用的
 */
class RequestInterceptor : Interceptor {

    companion object {
        @JvmField
        var sobCaptchaKey: String? = null
    }


    val SOB_CAPTCHA_KEY_NAME: String = "l_c_i"

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val buildre = request.newBuilder();

        val response = chain.proceed(buildre.build())
        val responseHeaders = response.headers

        val url = request.url.toString();

        if (url.contains("uc/ut/captcha")) {

            sobCaptchaKey = responseHeaders[SOB_CAPTCHA_KEY_NAME].toString()
        }

        return response

    }
}