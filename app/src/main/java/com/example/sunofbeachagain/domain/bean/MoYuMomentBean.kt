package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName

data class MoYuMomentBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MoYuMomentData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MoYuMomentData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("company")
    val company: Any,
    @SerializedName("content")
    val content: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("hasThumbUp")
    val hasThumbUp: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("linkCover")
    val linkCover: Any,
    @SerializedName("linkTitle")
    val linkTitle: Any,
    @SerializedName("linkUrl")
    val linkUrl: Any,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("position")
    val position: Any,
    @SerializedName("thumbUpCount")
    val thumbUpCount: Int,
    @SerializedName("thumbUpList")
    val thumbUpList: List<Any>?,
    @SerializedName("topicId")
    val topicId: String,
    @SerializedName("topicName")
    val topicName: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean
)