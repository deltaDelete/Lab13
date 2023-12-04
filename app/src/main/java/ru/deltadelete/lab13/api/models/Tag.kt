package ru.deltadelete.lab13.api.models

data class Tag(
    val tagId: Int,
    val name: String,
    val description: String,
    val isNsfw: Boolean
) {
    override fun toString(): String {
        return name
    }
}
