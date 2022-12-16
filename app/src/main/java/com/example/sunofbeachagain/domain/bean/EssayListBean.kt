package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName


data class EssayListBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: EssayListData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,
)

data class EssayListData(
    @SerializedName("currentPage")
    val currentPage: Double,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<EssayList>,
    @SerializedName("pageSize")
    val pageSize: Double,
    @SerializedName("total")
    val total: Double,
    @SerializedName("totalPage")
    val totalPage: Double,
)

data class EssayList(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("covers")
    val covers: List<String>,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isVip")
    val isVip: Boolean,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("thumbUp")
    val thumbUp: Double,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Double,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("viewCount")
    val viewCount: Double,
    @SerializedName("nickname")
    val nickname: String,
)