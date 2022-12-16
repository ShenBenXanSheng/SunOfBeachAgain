package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class UserInfoBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: UserInfoData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class UserInfoData(
    @SerializedName("area")
    val area: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("company")
    val company: String?,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("position")
    val position: String?,
    @SerializedName("sign")
    val sign: String?,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean,

)