package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageUpUpBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageUpUpListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageUpUpListData(
    @SerializedName("content")
    val content: List<MessageUpUpData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: MessageUpUpPageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: MessageUpUpSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class MessageUpUpData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("beUid")
    val beUid: String,
    @SerializedName("hasRead")
    val hasRead: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("thumbTime")
    val thumbTime: String,
    @SerializedName("timeText")
    val timeText: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("url")
    val url: String
)

data class MessageUpUpPageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: MessageUpUpSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class MessageUpUpSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)