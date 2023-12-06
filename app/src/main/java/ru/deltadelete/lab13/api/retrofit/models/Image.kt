package ru.deltadelete.lab13.api.retrofit.models

import java.util.Date

data class Image(
    val signature: String,
    val extension: String,
    val imageId: Int,
    val dominantColor: String,
    val source: String,
    val artist: Artist,
    val uploadedAt: Date,
    val isNsfw: Boolean,
    val width: Int,
    val height: Int,
    val byteSize: Int,
    val url: String,
    val previewUrl: String,
    val tags: List<Tag>
)
