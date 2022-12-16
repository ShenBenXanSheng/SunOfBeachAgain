package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MoYuListBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MoYuListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MoYuListData(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<MoYuData>,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)

data class MoYuData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("company")
    val company: String?,
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
    val linkCover: String?,
    @SerializedName("linkTitle")
    val linkTitle: String?,
    @SerializedName("linkUrl")
    val linkUrl: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("position")
    val position: String?,
    @SerializedName("thumbUpCount")
    val thumbUpCount: Int,
    @SerializedName("thumbUpList")
    val thumbUpList: List<String>?,
    @SerializedName("topicId")
    val topicId: String?,
    @SerializedName("topicName")
    val topicName: String?,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean
)