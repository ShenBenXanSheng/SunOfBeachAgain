package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class UserCenterEssayBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: UserCenterEssayData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class UserCenterEssayData(
    @SerializedName("currentPage")
    val currentPage: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPre")
    val hasPre: Boolean,
    @SerializedName("list")
    val list: List<EssayList>,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPage")
    val totalPage: Int
)

