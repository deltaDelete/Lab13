package ru.deltadelete.lab13.api.retrofit.models

import java.io.Serializable

data class Tag(
    val tag_id: Int,
    val name: String,
    val description: String,
    val is_nsfw: Boolean
) : Serializable {
    override fun toString(): String {
        return name
    }
}
