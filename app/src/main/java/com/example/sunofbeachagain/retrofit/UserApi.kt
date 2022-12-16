package com.example.sunofbeachagain.retrofit

import com.example.sunofbeachagain.domain.bean.*
import com.example.sunofbeachagain.domain.body.ChangeUserInfoBody
import com.example.sunofbeachagain.domain.body.CreateShouCangBody
import com.example.sunofbeachagain.domain.body.ShouCangActionBody
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/ast/achievement/{userId}")
    suspend fun getUserAchievement(@Path("userId") userId: String): UserAchievementBean

    @GET("/uc/user-info/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): UserInfoBean

    @GET("/uc/ucenter/user-info")
    suspend fun getDetailedUserInfo(@Header("sob_token") token:String):DetailedUserInfoBean

    @GET("/uc/user/logout")
    suspend fun exitLogin(@Header("sob_token") token: String)

    @GET("/ct/moyu/list/user/{userID}/{page}")
    suspend fun getUserCenterMoYuData(
        @Path("userID") userId: String,
        @Path("page") page: Int,
    ): UserCenterMoYuBean

    @GET("/ct/article/list/{userID}/{page}")
    suspend fun getUserCenterEssayData(
        @Path("userID") userId: String,
        @Path("page") page: Int,
    ): UserCenterEssayBean

    @GET("/uc/follow/list/{userId}/{page}")
    suspend fun getUserFollowData(
        @Header("sob_token") token: String,
        @Path("userId") userId: String,
        @Path("page") page: Int,
    ): UserFollowAndFansBean


    @GET("/uc/fans/list/{userId}/{page}")
    suspend fun getUserFansData(
        @Header("sob_token") token: String,
        @Path("userId") userId: String,
        @Path("page") page: Int,
    ): UserFollowAndFansBean

    @GET("/uc/fans/state/{userId}")
    suspend fun getUserFansState(
        @Header("sob_token") token: String,
        @Path("userId") userId: String,
    ): UserFansStateBean

    @GET("/ast/rank/sob/10")
    suspend fun getSobRankingsData(): SobRankingsBean


    //================================收藏================================
    @GET("/ct/ucenter/collection/list/{page}")
    suspend fun getShouCangListData(
        @Header("sob_token") token: String,
        @Path("page") page: Int,
    ): ShouCangListBean

    @GET("/ct/ucenter/favorite/list/{collectionId}/{page}/{order}")
    suspend fun getShouCangDetailData(
        @Header("sob_token") token: String,
        @Path("collectionId") collectionId: String,
        @Path("page") page: Int,
        @Path("order") order:String
    ): UserShouCangDetailBean

    @Multipart
    @POST("/oss/image/collection")
    suspend fun postShouCangCover(@Header("sob_token") token:String ,@Part multipart: MultipartBody.Part):SobBean

    @POST("/ct/ucenter/collection")
    suspend fun postCreateShouCang(@Header("sob_token") token: String,@Body createShouCangBody: CreateShouCangBody):SobBean

    @POST("/ct/favorite")
    suspend fun postAddShouCang(@Header("sob_token") token: String,@Body shouCangActionBody: ShouCangActionBody):SobBean

    @GET("/ct/favorite/checkCollected")
    suspend fun checkHasShouCang(@Header("sob_token") token: String,@Query("url") url:String):SobBean

    @DELETE("/ct/favorite/{favoriteId}")
    suspend fun deleteShouCang(@Header("sob_token")token: String,@Path("favoriteId") favoriteId:String):SobBean


    //================================修改用户信息================================
    @GET("/uc/ucenter/user-info/user-name")
    suspend fun checkNicknameCanUser(@Query("userName") userName:String):SobBean

    @PUT("/uc/ucenter/user-info/sex-nickname")
    suspend fun changeUserNicknameAndSex(@Header("sob_token") token:String,@Query("sex") sex:String,@Query("nickname") nickname:String):SobBean

    @PUT("/uc/ucenter/user-info")
    suspend fun changeUserInfo(@Header("sob_token") token:String,@Body changeUserInfoBody: ChangeUserInfoBody):SobBean

    @GET("/ws/district/v1/list?key=YECBZ-R7EK4-DHUUH-XJOHP-GCNR6-QPFCS")
    suspend fun getUserPrice():UserPriceBean

    @Multipart
    @POST("/oss/image/avatar")
    suspend fun postAvatarImage(@Header("sob_token") token: String,@Part multipart: MultipartBody.Part):SobBean

    @PUT("/uc/ucenter/user-info/avatar")
    suspend fun putUserAvatar(@Header("sob_token") token: String,@Query("avatar") avatar:String):SobBean

}