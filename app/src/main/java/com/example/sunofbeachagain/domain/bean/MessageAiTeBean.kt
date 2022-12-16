package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageAiTeBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageAiTeListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageAiTeListData(
    @SerializedName("content")
    val content: List<MessageAiTeData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: MessageAiTePageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: MessageAiTeSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class MessageAiTeData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("beNickname")
    val beNickname: String,
    @SerializedName("beUid")
    val beUid: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("exId")
    val exId: String,
    @SerializedName("hasRead")
    val hasRead: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("timeText")
    val timeText: Any,
    @SerializedName("type")
    val type: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("url")
    val url: String
)

data class MessageAiTePageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: MessageAiTeSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class MessageAiTeSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)