package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class WebCheckImageBean(
    @SerializedName("data")
    val `data`: WebCheckImageData,
    @SerializedName("protocolCode")
    val protocolCode: Int
)

data class WebCheckImageData(
    @SerializedName("imageUrl")
    val imageUrl: String
)