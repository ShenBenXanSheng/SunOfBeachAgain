package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class SobRankingsBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<SobRankingData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class SobRankingData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("isVip")
    val isVip: Boolean,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("sob")
    val sob: Double,
    @SerializedName("userId")
    val userId: String
)