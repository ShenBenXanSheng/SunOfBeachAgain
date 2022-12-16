package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface MessageApi {

    @GET("/ct/ucenter/message/wenda/{page}")
    suspend fun getMessageWenDaData(@Header("sob_token") token:String,@Path("page") page:Int):MessageWenDaBean

    @GET("/ct/ucenter/message/thumb/{page}")
    suspend fun getMessageUpUpData(@Header("sob_token") token:String,@Path("page") page:Int):MessageUpUpBean

    @GET("/ct/ucenter/message/moment/{page}")
    suspend fun getMessageMoYuData(@Header("sob_token") token:String,@Path("page") page:Int):MessageMoYuBean

    @GET("/ct/ucenter/message/at/{page}")
    suspend fun getMessageAiTeData(@Header("sob_token") token:String,@Path("page") page:Int):MessageAiTeBean

    @GET("/ct/ucenter/message/system/{page}")
    suspend fun getMessageSystemData(@Header("sob_token") token:String,@Path("page") page:Int):MessageSystemBean

    @GET("/ct/msg/count")
    suspend fun getMessageCount(@Header("sob_token") token:String):MessageCountBean

    @GET("/ct/msg/read")
    suspend fun readAllMessage(@Header("sob_token")token:String):SobBean

    @PUT("/ct/ucenter/message/moment/state/{msgId}/1")
    suspend fun putMoYuNewMessage(@Header("sob_token")token:String,@Path("msgId") msgId:String):SobBean

    @PUT("/ct/ucenter/message/wenda/state/{id}/1")
    suspend fun putWenDaNewMessage(@Header("sob_token")token:String,@Path("id") id:String):SobBean

    @PUT("/ct/ucenter/message/at/state/{msgId}/1")
    suspend fun putAiTeNewMessage(@Header("sob_token")token:String,@Path("msgId") msgId:String):SobBean
}