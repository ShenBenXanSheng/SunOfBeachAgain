package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class UserFollowAndFansBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: UserFollowAndFansData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class UserFollowAndFansData(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<UserFollowAndFans>,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)

data class UserFollowAndFans(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("relative")
    val relative: Int,
    @SerializedName("sign")
    val sign: String?,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("vip")
    val vip: Boolean
)