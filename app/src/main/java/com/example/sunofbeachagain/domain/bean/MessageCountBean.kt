package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageCountBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageCountData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageCountData(
    @SerializedName("articleMsgCount")
    val articleMsgCount: Int,
    @SerializedName("atMsgCount")
    val atMsgCount: Int,
    @SerializedName("momentCommentCount")
    val momentCommentCount: Int,
    @SerializedName("shareMsgCount")
    val shareMsgCount: Int,
    @SerializedName("systemMsgCount")
    val systemMsgCount: Int,
    @SerializedName("thumbUpMsgCount")
    val thumbUpMsgCount: Int,
    @SerializedName("wendaMsgCount")
    val wendaMsgCount: Int
)