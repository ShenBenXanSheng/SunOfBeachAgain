package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class WenDaDetailCommentBean(
    @SerializedName("data")
    val `data`: WenDaDetailCommentData,
    @SerializedName("protocolCode")
    val protocolCode: Int
)

data class WenDaDetailCommentData(
    @SerializedName("wendaId")
    val wendaId: String
)