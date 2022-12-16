package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class MessageSystemBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: MessageSystemListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class MessageSystemListData(
    @SerializedName("content")
    val content: List<MessageSystemData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: MessageSystemPageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: MessageSystemSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class MessageSystemData(
    @SerializedName("content")
    val content: String,
    @SerializedName("exId")
    val exId: Any,
    @SerializedName("exType")
    val exType: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("publishTime")
    val publishTime: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: String
)

data class MessageSystemPageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: MessageSystemSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class MessageSystemSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)