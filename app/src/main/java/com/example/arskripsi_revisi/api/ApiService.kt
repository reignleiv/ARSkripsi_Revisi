package com.example.arskripsi_revisi.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("models/{filename}")
    fun getModel(@Path("filename") filename: String): Call<ResponseBody>

    @GET("models")
    fun getModelList(): Call<List<String>>
}

object RetrofitClient {
    private const val BASE_URL = "https://arskripsi.irnhakim.com/public/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}