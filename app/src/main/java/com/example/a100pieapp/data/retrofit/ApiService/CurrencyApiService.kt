package com.example.a100pieapp.data.retrofit.ApiService

import androidx.lifecycle.MutableLiveData
import com.example.a100pieapp.data.model.Currencies
import com.example.a100pieapp.data.model.CurrencyHolder
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {

    @GET("getcurrencies")
    fun getCurrencies(): Deferred<CurrencyHolder>
}