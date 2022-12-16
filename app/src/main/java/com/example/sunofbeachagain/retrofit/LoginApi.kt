package com.example.sunofbeachagain.retrofit


import com.example.sunofbeachagain.domain.bean.CheckTokenBean
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.PhoneCodeBody
import com.example.sunofbeachagain.domain.body.EnrollSobBody
import com.example.sunofbeachagain.domain.body.ForgetPasswordBody
import com.example.sunofbeachagain.domain.body.UserBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {
    @GET("/uc/user/avatar/{phoneNum}")
    suspend fun getUserAvatarByPhoneNumber(@Path("phoneNum") phoneNum: String): SobBean

    @POST("/uc/user/login/{captcha}")
     fun postLogin(
        @Header("l_c_i") lci: String,
        @Path("captcha") captcha: String,
        @Body userBody: UserBody,
    ): Call<SobBean>

    @GET("/uc/user/checkToken")
     suspend fun checkToken(@Header("sob_token") token:String):CheckTokenBean

     @POST("/uc/ut/join/send-sms")
     suspend fun postPhoneNumberCode(@Header("l_c_i") lci: String,@Body phoneCodeBody: PhoneCodeBody):SobBean

    @GET("/uc/ut/check-sms-code/{phoneNumber}/{smsCode}")
    suspend fun checkPhoneCodeIsTrue(@Path("phoneNumber") phoneNumber:String,@Path("smsCode") smsCode:String):SobBean

    @POST("/uc/user/register/{smsCode}")
    suspend fun enrollSob(@Path("smsCode") smsCode:String,@Body enrollSobBody: EnrollSobBody):SobBean

    @POST("/uc/ut/forget/send-sms")
    suspend fun postForgetPhoneNumberCode(@Header("l_c_i") lci:String,@Body phoneCodeBody: PhoneCodeBody):SobBean

    @PUT("/uc/user/forget/{smsCode}")
    suspend fun forgetPassword(@Path("smsCode") smsCode: String,@Body forgetPasswordBody: ForgetPasswordBody):SobBean

}