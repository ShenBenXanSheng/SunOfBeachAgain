package com.example.sunofbeachagain.domain.body

data class MoYuSubCommentBody(
    val content: String,
    val momentId: String,
    val targetUserId: String,
    val commentId: String,
)
