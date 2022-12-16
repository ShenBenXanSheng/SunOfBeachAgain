package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class WebUpAndTippingAndComment(
    @SerializedName("data")
    val `data`: WebEssayClickData,
    @SerializedName("protocolCode")
    val protocolCode: Int
)

data class WebEssayClickData(
    @SerializedName("articleId")
    val articleId: String
)