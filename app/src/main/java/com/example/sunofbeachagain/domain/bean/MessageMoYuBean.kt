package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageMoYuBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageMoYuListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageMoYuListData(
    @SerializedName("content")
    val content: List<MessageMoYuData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: MessageMoYuPageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: MessageMoYuSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class MessageMoYuData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("bUid")
    val bUid: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("hasRead")
    val hasRead: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("momentId")
    val momentId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("timeText")
    val timeText: Any,
    @SerializedName("title")
    val title: String,
    @SerializedName("uid")
    val uid: String
)

data class MessageMoYuPageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: MessageMoYuSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class MessageMoYuSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)