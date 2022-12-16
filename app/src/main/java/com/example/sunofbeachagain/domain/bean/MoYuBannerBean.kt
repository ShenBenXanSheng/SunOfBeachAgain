package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MoYuBannerBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<MoYuBannerData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MoYuBannerData(
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("picUrl")
    val picUrl: String,
    @SerializedName("targetUrl")
    val targetUrl: String,
    @SerializedName("type")
    val type: String
)