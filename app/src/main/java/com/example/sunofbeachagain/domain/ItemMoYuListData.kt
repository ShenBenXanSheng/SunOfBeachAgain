package com.example.sunofbeachagain.domain

data class ItemMoYuListData(
    val avatar: String,
    val nickname: String,
    val position: String,
    val createTime: String,
    val content:String,
    val imageList:List<String>?,
    val linkTitle:String?,
    val linkUrl:String?,
    val theme:String,
    val huifuCount:String,
    val upupCount:String
) {
}