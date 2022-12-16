package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName


data class MoYuCommentBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MoYuCommentData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
)

data class MoYuCommentData(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<MoYuComment>,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int,
)

data class MoYuComment(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("momentId")
    val momentId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("subComments")
    val subComments: List<SubComment>,
    @SerializedName("thumbUp")
    val thumbUp: Int,
    @SerializedName("thumbUpList")
    val thumbUpList: List<Any>,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean,
) : java.io.Serializable

data class SubComment(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("commentId")
    val commentId: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("targetUserId")
    val targetUserId: String,
    @SerializedName("targetUserIsVip")
    val targetUserIsVip: Boolean,
    @SerializedName("targetUserNickname")
    val targetUserNickname: String,
    @SerializedName("thumbUpList")
    val thumbUpList: List<Any>,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean,
): java.io.Serializable