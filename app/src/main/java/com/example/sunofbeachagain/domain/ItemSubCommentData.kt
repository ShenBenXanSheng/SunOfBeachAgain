package com.example.sunofbeachagain.domain

data class ItemSubCommentData(
    val avatar: String,
    val nickname: String,
    val position: String?,
    val createTime: String,
    val targetName:String,
    val content:String,
    val userId:String,
    val momentId:String,
    val commentId:String
) {
}