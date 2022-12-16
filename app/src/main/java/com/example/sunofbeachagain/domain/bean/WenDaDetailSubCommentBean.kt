package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class WenDaDetailSubCommentBean(
    @SerializedName("data")
    val `data`: WenDaDetailSubCommentData,
    @SerializedName("protocolCode")
    val protocolCode: Int
)

data class WenDaDetailSubCommentData(
    @SerializedName("beNickname")
    val beNickname: String,
    @SerializedName("beUid")
    val beUid: String,
    @SerializedName("parentId")
    val parentId: String,
    @SerializedName("wendaId")
    val wendaId: String
):java.io.Serializable