package com.example.sunofbeachagain.domain.body

data class QuestionSubCommentBody(
    val content: String,
    val parentId: String,
    val beNickname: String,
    val beUid: String,
    val wendaId: String,
) {
}