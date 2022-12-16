package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.*
import com.example.sunofbeachagain.domain.body.MoYuCommentBody
import com.example.sunofbeachagain.domain.body.MoYuMomentBody
import com.example.sunofbeachagain.domain.body.MoYuSubCommentBody
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MoYuApi {
    @GET("/ast/home/loop/list")
    suspend fun getMoYuBanner(): MoYuBannerBean

    @GET("/ct/moyu/list/recommend/{page}")
    suspend fun getMoYuListRecommendData(@Path("page") page: Int):MoYuListBean

    @GET("/ct/moyu/list/follow/{page}")
    suspend fun getMoYuListFollowData(@Header("sob_token") token:String,@Path("page") page: Int):MoYuListBean

    @GET("/ct/moyu/{momentId}")
    suspend fun getSingleMoYuData(@Path("momentId") momentId:String): MoYuSingleBean

    @GET("/ct/moyu/comment/{momentId}/{page}?sort=1")
    suspend fun getMoYuDetailComment(@Path("momentId") momentId:String,@Path("page") page:Int):MoYuCommentBean

    @GET("/ct/moyu/topic")
    suspend fun getTopicData():TopicBean

    @Multipart
    @POST("/oss/image/mo_yu")
    suspend fun postMoYuImage(@Header("sob_token") token:String,@Part part: MultipartBody.Part ):SobBean

    @POST("/ct/moyu")
    suspend fun postMoYuMoment(@Header("sob_token") token:String,@Body moYuMomentBody: MoYuMomentBody):MoYuMomentBean

    @POST("/ct/moyu/comment")
    suspend fun postMoYuComment(@Header("sob_token") token:String,@Body moYuCommentBody: MoYuCommentBody):SobBean

    @POST("/ct/moyu/sub-comment")
    suspend fun postMoYuSubComment(@Header("sob_token") token:String,@Body momentSubComment:MoYuSubCommentBody):SobBean

    @PUT("/ct/moyu/thumb-up/{momentId}")
    suspend fun putMoYuMomentUp(@Header("sob_token") token:String,@Path("momentId") momentId:String):SobBean
}