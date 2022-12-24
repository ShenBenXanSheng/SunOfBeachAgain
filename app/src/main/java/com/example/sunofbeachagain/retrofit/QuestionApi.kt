package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.QuestionListBean
import com.example.sunofbeachagain.domain.bean.QuestionRankingsBean
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.body.QuestionCommentBody
import com.example.sunofbeachagain.domain.body.QuestionSubCommentBody
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface QuestionApi {
    //获得问答列表
    @GET("/ct/wenda/list")
    suspend fun getQuestionList(
        @Query("page") page: Int,
        @Query("state") state: String,
        @Query("category") category: String,
    ): QuestionListBean

    //获得问答排行榜
    @GET("/ast/rank/answer-count/10")
    suspend fun getQuestionRankings(): QuestionRankingsBean

    //评论
    @POST("/ct/wenda/comment")
    suspend fun postQuestionComment(@Header("sob_token") token:String,@Body questionCommentBody: QuestionCommentBody):SobBean

    //回复
    @POST("/ct/wenda/sub-comment")
    suspend fun postQuestionSubComment(@Header("sob_token") token: String,@Body questionSubCommentBody: QuestionSubCommentBody):SobBean
}