package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName


data class QuestionListBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: QuestionListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
)

data class QuestionListData(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<QuestionData>,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int,
)

data class QuestionData(
    @SerializedName("answerCount")
    val answerCount: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("categoryId")
    val categoryId: String?,
    @SerializedName("categoryName")
    val categoryName: String?,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isResolve")
    val isResolve: String?,
    @SerializedName("isVip")
    val isVip: String?,
    @SerializedName("label")
    val label: Any?,
    @SerializedName("labels")
    val labels: List<String>?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("sob")
    val sob: Int,
    @SerializedName("state")
    val state: Any?,
    @SerializedName("thumbUp")
    val thumbUp: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("viewCount")
    val viewCount: Int,
) {
    constructor(
        answerCount: Int,
        avatar: String,
        createTime: String,
        id: String,
        nickname: String,
        sob: Int,
        thumbUp: Int,
        title: String,
        userId: String,
        viewCount: Int,
    ) : this(answerCount,
        avatar,
        categoryId = null,
        categoryName = null,
        createTime,
        id,
        isResolve = null,
        isVip = null,
        label = null, labels = null, nickname, sob, state = null, thumbUp, title, userId, viewCount)
}