package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class DetailedUserInfoBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DetailedUserInfoData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class DetailedUserInfoData(
    @SerializedName("area")
    val area: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("goodAt")
    val goodAt: String,
    @SerializedName("isvIP")
    val isvIP: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("phoneNum")
    val phoneNum: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("sex")
    val sex: Int,
    @SerializedName("sign")
    val sign: String,
    @SerializedName("userId")
    val userId: String
)