package com.example.sunofbeachagain.domain.body

data class EssaySubCommentBody(
    val articleId: String,
    val parentId: String,
    val beUid: String,
    val beNickname: String,
    val content: String,
) {
}