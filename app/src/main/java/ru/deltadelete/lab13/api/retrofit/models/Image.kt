package ru.deltadelete.lab13.api.retrofit.models

import java.io.Serializable
import java.util.Date

data class Image(
    val signature: String,
    val extension: String,
    val image_id: Int,
    val dominant_color: String,
    val source: String,
    val artist: Artist?,
    val uploaded_at: Date,
    val is_nsfw: Boolean,
    val width: Int,
    val height: Int,
    val byte_size: Int,
    val url: String,
    val preview_url: String,
    val tags: List<Tag>
) : Serializable
