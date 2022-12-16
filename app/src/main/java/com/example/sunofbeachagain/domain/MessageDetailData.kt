package com.example.sunofbeachagain.domain

data class MessageDetailData(
    val avatar: String,
    val nickname: String,
    val createTime: String,
    val content: String,
    val title: String,
    val id: String,
    val type:String,
    val userId:String,
    val readState:Int,
    val isAiTe:Boolean,
    val msgId:String
) {
}