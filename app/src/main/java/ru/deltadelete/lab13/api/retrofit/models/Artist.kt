package ru.deltadelete.lab13.api.retrofit.models

data class Artist(
    val artistId: Int,
    val name: String,
    val patreon: String,
    val pixiv: String,
    val twitter: String,
    val deviantArt: String
)