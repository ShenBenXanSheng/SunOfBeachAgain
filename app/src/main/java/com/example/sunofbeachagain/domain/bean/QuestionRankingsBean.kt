package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class QuestionRankingsBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<QuestionRankingsData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class QuestionRankingsData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean
)