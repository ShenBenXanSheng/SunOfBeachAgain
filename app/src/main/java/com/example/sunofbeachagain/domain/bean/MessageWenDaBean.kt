package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageWenDaBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageWenDaListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageWenDaListData(
    @SerializedName("content")
    val content: List<MessageWenDaData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: Pageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: SortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class MessageWenDaData(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("bUid")
    val bUid: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("hasRead")
    val hasRead: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("timeText")
    val timeText: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("wendaId")
    val wendaId: String
)

data class Pageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: SortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class SortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)