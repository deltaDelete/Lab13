package ru.deltadelete.lab13.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.deltadelete.lab13.api.models.Response
import ru.deltadelete.lab13.api.models.Tag

interface WaifuImRepository {
    @GET("/search")
    fun search(
        @Query("included_tags") includedTags: List<Tag>,
        @Query("excluded_tags") excludedTags: List<Tag>,
        @Query("included_files") includedFiles: List<Int>,
        @Query("excluded_files") excludedFiles: List<Int>,
        @Query("is_nsfw") isNsfw: Boolean = false,
        @Query("gif") gif: Boolean? = null,

    ): Call<Response>
}