package com.example.a100pieapp.data.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MyRetrofit {

    private var baseUrl = "https://bittrex.com/api/v1.1/public/"

    fun getInstance(): Retrofit {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(2, TimeUnit.SECONDS)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}