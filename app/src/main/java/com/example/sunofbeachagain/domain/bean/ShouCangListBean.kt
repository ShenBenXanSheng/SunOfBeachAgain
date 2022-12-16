package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class ShouCangListBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ShouCangListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class ShouCangListData(
    @SerializedName("content")
    val content: List<ShouCangData>,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: ShouCangPageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: ShouCangSortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class ShouCangData(
    @SerializedName("avatar")
    val avatar: Any,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("favoriteCount")
    val favoriteCount: Int,
    @SerializedName("followCount")
    val followCount: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("permission")
    val permission: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: Any
)

data class ShouCangPageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: ShouCangSortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class ShouCangSortX(
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)