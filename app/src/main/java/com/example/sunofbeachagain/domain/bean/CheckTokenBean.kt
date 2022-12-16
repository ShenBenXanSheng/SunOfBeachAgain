package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class CheckTokenBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: CheckTokenData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class CheckTokenData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("fansCount")
    val fansCount: Any,
    @SerializedName("followCount")
    val followCount: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("isVip")
    val isVip: String,
    @SerializedName("lev")
    val lev: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("roles")
    val roles: Any,
    @SerializedName("token")
    val token: Any
)