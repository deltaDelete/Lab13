package ru.deltadelete.lab13.api

import android.graphics.drawable.GradientDrawable.Orientation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.deltadelete.lab13.api.models.Response
import ru.deltadelete.lab13.api.models.Tag

interface WaifuImRepository {
    @GET("/search")
    fun search(
        @Query("included_tags") includedTags: List<Tag> = emptyList(),
        @Query("excluded_tags") excludedTags: List<Tag> = emptyList(),
        @Query("included_files") includedFiles: List<Int> = emptyList(),
        @Query("excluded_files") excludedFiles: List<Int> = emptyList(),
        @Query("is_nsfw") isNsfw: Boolean = false,
        @Query("gif") gif: Boolean? = null,
        @Query("orientation") orientation: Orientation? = null,
        @Query("orientation") orderBy: OrderBy? = null,
        @Query("many") many: Boolean? = null,
        @Query("width") width: OperatedInteger? = null,
        @Query("height") height: OperatedInteger? = null,
        @Query("byte_size") byteSize: OperatedInteger? = null,
    ): Call<Response>

    enum class Orientation {
        Portrait,
        Landscape,
        Random;

        override fun toString(): String {
            return name
        }
    }

    enum class OrderBy(private val value: String) {
        Favourites("favourites"),
        UploadedAt("uploaded_at"),
        Random("random");

        override fun toString(): String {
            return value
        }
    }

    class OperatedInteger(
        val value: Int = 0,
        val operator: Operator = Operator.Equal
    ) {
        override fun toString(): String {
            return "$operator$value"
        }
    }

    enum class Operator(private val value: String) {
        Equal("="),
        NotEqual("!="),
        GreaterThan(">"),
        LessThan("<"),
        GreaterThanOrEqual(">="),
        LessThanOrEqual("<=");

        override fun toString(): String {
            return value
        }
    }
}