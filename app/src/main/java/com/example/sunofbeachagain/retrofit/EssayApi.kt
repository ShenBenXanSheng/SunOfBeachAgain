package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.EssayCategoryBean
import com.example.sunofbeachagain.domain.bean.EssayListBean
import com.example.sunofbeachagain.domain.bean.SobBean
import com.example.sunofbeachagain.domain.bean.UserAchievementBean
import com.example.sunofbeachagain.domain.body.EssayCommentBody
import com.example.sunofbeachagain.domain.body.EssaySubCommentBody
import com.example.sunofbeachagain.domain.body.EssayTippingBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EssayApi {
    //获得文章类别
    @GET("/ct/category/list")
    suspend fun getEssayCategory(): EssayCategoryBean

    //获得文章推荐列表
    @GET("/ct/content/home/recommend/{page}")
    suspend fun getEssayListRecommendData(@Path("page") page: Int): EssayListBean

    //根据文章类别获得文章列表
    @GET("/ct/content/home/recommend/{categoryId}/{page}")
    suspend fun getEssayListByCategoryId(
      @Path("categoryId") categoryId: String,
      @Path("page") page: Int,
    ): EssayListBean

    //检查文章是否已经点赞
    @GET("/ct/article/check-thumb-up/{essayId}")
    suspend fun checkEssayHasUp(@Header("sob_token") token:String,@Path("essayId") essayId:String):SobBean

    //点赞文章
    @PUT("/ct/article/thumb-up/{essayId}")
    suspend fun putEssayUpUp(@Header("sob_token") token:String,@Path("essayId") essayId: String):SobBean

    //获得个人成就
    @GET("/ast/achievement")
    suspend fun getAchievement(@Header("sob_token") token: String): UserAchievementBean

    //打赏文章
    @POST("/ast/prise/article")
    suspend fun postEssayTipping(@Header("sob_token") token: String,@Body essayTippingBody: EssayTippingBody):SobBean

    //评论文章
    @POST("/ct/article/comment")
    suspend fun postEssayComment(@Header("sob_token") token: String,@Body essayCommentBody: EssayCommentBody):SobBean

    //回复文章
    @POST("/ct/article/sub-comment")
    suspend fun postEssaySubComment(@Header("sob_token") token: String,@Body essaySubCommentBody: EssaySubCommentBody):SobBean
}