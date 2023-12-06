package ru.deltadelete.lab13.api.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.deltadelete.lab13.api.retrofit.interfaces.WaifuImRepository

object RetrofitClient {
    private val BASE_URL = "https://api.waifu.im/"

    val client by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val waifuRepo: WaifuImRepository
        get() = client.create(WaifuImRepository::class.java)
}
