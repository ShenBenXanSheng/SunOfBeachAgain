package com.example.sunofbeachagain.domain.body

data class ChangeUserInfoBody(
    val area: String,
    val company: String,
    val goodAt: String,
    val position: String,
    val sign: String,
    val userId: String,
)