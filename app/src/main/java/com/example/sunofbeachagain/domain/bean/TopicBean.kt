package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName


data class TopicBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<TopicData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
)

data class TopicData(
    @SerializedName("contentCount")
    val contentCount: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("followCount")
    val followCount: Int,
    @SerializedName("hasFollowed")
    val hasFollowed: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("topicName")
    val topicName: String,
)