package com.example.sunofbeachagain.domain.bean

import com.google.gson.annotations.SerializedName

data class UserPriceBean (
    @SerializedName("data_version")
    val dataVersion: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: List<List<Result>>,
    @SerializedName("status")
    val status: Int
)

data class Result(
    @SerializedName("cidx")
    val cidx: List<Int>,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String,
    @SerializedName("pinyin")
    val pinyin: List<String>
)

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
