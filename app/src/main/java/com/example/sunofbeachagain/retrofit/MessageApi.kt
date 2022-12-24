package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface MessageApi {

    //获得问答消息
    @GET("/ct/ucenter/message/wenda/{page}")
    suspend fun getMessageWenDaData(@Header("sob_token") token:String,@Path("page") page:Int):MessageWenDaBean

    //获得点赞消息
    @GET("/ct/ucenter/message/thumb/{page}")
    suspend fun getMessageUpUpData(@Header("sob_token") token:String,@Path("page") page:Int):MessageUpUpBean

    //获得摸鱼消息
    @GET("/ct/ucenter/message/moment/{page}")
    suspend fun getMessageMoYuData(@Header("sob_token") token:String,@Path("page") page:Int):MessageMoYuBean

    //获得@消息
    @GET("/ct/ucenter/message/at/{page}")
    suspend fun getMessageAiTeData(@Header("sob_token") token:String,@Path("page") page:Int):MessageAiTeBean

    //获得系统消息
    @GET("/ct/ucenter/message/system/{page}")
    suspend fun getMessageSystemData(@Header("sob_token") token:String,@Path("page") page:Int):MessageSystemBean

    //获得消息数量
    @GET("/ct/msg/count")
    suspend fun getMessageCount(@Header("sob_token") token:String):MessageCountBean

    //一键已读所有消息
    @GET("/ct/msg/read")
    suspend fun readAllMessage(@Header("sob_token")token:String):SobBean

    //已读摸鱼消息
    @PUT("/ct/ucenter/message/moment/state/{msgId}/1")
    suspend fun putMoYuNewMessage(@Header("sob_token")token:String,@Path("msgId") msgId:String):SobBean

    //已读问答消息
    @PUT("/ct/ucenter/message/wenda/state/{id}/1")
    suspend fun putWenDaNewMessage(@Header("sob_token")token:String,@Path("id") id:String):SobBean

    //已读@消息
    @PUT("/ct/ucenter/message/at/state/{msgId}/1")
    suspend fun putAiTeNewMessage(@Header("sob_token")token:String,@Path("msgId") msgId:String):SobBean
}