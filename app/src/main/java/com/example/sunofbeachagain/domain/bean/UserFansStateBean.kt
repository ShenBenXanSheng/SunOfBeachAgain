package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class UserFansStateBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)