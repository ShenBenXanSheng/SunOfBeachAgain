package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitUtil {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.SOB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val loginApi = retrofit.create(LoginApi::class.java)

    val moYuApi = retrofit.create(MoYuApi::class.java)

    val essayApi = retrofit.create(EssayApi::class.java)

    val questionApi = retrofit.create(QuestionApi::class.java)

    val messageApi = retrofit.create(MessageApi::class.java)

    val userApi = retrofit.create(UserApi::class.java)


    private val retrofitPrice = Retrofit.Builder()
        .baseUrl("https://apis.map.qq.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userPriceApi = retrofitPrice.create(UserApi::class.java)
}