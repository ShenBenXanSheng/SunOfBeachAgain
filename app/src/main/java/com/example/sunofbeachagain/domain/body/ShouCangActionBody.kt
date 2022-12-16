package com.example.sunofbeachagain.domain.body

data class ShouCangActionBody(
    val collectionId: String,
    val title: String,
    val url: String,
    val type: String,
    val cover: String?,
) {
}