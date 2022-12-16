package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class UserShouCangDetailBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: UserShouCangDetailData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class UserShouCangDetailData(
    @SerializedName("content")
    val content: List<UserShouCangDetail>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: UserShouCangDetailPageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: UserShouCangDetailSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class UserShouCangDetail(
    @SerializedName("addTime")
    val addTime: String,
    @SerializedName("avatar")
    val avatar: Any,
    @SerializedName("collectionId")
    val collectionId: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: Any
)

data class UserShouCangDetailPageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: UserShouCangDetailSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class UserShouCangDetailSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)