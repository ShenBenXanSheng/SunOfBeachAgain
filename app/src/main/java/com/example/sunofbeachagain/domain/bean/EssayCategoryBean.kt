package com.example.sunofbeachagain.domain.bean
import com.google.gson.annotations.SerializedName


data class EssayCategoryBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<EssayCategoryData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class EssayCategoryData(
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("order")
    val order: Double,
    @SerializedName("pyName")
    val pyName: String
)