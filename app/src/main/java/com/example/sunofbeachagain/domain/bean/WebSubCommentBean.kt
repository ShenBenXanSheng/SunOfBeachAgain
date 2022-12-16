package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class WebSubCommentBean(
    @SerializedName("data")
    val `data`: WebSubCommentData,
    @SerializedName("protocolCode")
    val protocolCode: Int
)

data class WebSubCommentData(
    @SerializedName("articleId")
    val articleId: String,
    @SerializedName("beNickname")
    val beNickname: String,
    @SerializedName("beUid")
    val beUid: String,
    @SerializedName("parentId")
    val parentId: String
):java.io.Serializable