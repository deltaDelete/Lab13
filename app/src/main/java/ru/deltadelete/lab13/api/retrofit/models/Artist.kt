package ru.deltadelete.lab13.api.retrofit.models

import java.io.Serializable

data class Artist(
    val artist_id: Int,
    val name: String,
    val patreon: String,
    val pixiv: String,
    val twitter: String,
    val deviant_art: String
) : Serializable
