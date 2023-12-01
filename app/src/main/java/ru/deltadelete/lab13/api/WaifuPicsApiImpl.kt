package ru.deltadelete.lab13.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.ConnectionPool
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class WaifuPicsApiImpl : WaifuPicsApi {

    companion object {
        const val API_ENDPOINT = "https://api.waifu.pics"
        val JSON: MediaType = "application/json".toMediaType()
        val STRING_LIST_TYPE: Type = object : TypeToken<List<String>>() {}.type
        val body: RequestBody = "{}".toRequestBody(JSON)
    }

    private val client by lazy {
        OkHttpClient
            .Builder()
            .connectionPool(ConnectionPool(5, 10, TimeUnit.SECONDS))
            .build()
    }

    private val gson by lazy {
        Gson()
    }

    override fun getRandom(category: WaifuPicsApi.Categories): String {
        val request: Request = Request.Builder()
            .url("$API_ENDPOINT/sfw/${category.name}")
            .addHeader("Content-Type", "application/json")
            .get()
            .build()

        val result = client.newCall(request).execute().use { response ->
            response.body?.string() ?: ""
        }

        val jsonObject: JsonObject = gson.fromJson(result, JsonObject::class.java)
        return jsonObject.get("url").asString
    }

    // Ограничение API, нельзя выбрать количество картинок, всегда 30
    override fun getManyRandom(amount: Int, category: WaifuPicsApi.Categories): List<String> {
        val request: Request = Request.Builder()
            .url("$API_ENDPOINT/many/sfw/${category.name}")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        val result = client.newCall(request).execute().use { response ->
            response.body?.string() ?: "{\"files\": []}"
        }
        val jsonObject: JsonObject = gson.fromJson(result, JsonObject::class.java)
        return jsonObject.get("files").asJsonArray.map { jsonElement ->
            jsonElement.asString
        }
    }
}