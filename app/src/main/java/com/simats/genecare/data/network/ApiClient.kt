package com.simats.genecare.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object ApiClient {
    // REPLACE WITH YOUR COMPUTER'S LOCAL IP ADDRESS
    // For Emulator use "http://10.0.2.2/genecare/"
    // For Physical Device use your machine's IP, e.g., "http://10.164.172.174/genecare/"
    private const val BASE_URL = "http://192.168.137.1/genecare/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: GeneCareApi by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(GeneCareApi::class.java)
    }
}
