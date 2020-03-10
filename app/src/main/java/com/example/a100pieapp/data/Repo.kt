package com.example.a100pieapp.data

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.a100pieapp.data.model.Currencies
import com.example.a100pieapp.data.model.CurrencyHolder
import com.example.a100pieapp.data.retrofit.ApiService.CurrencyApiService
import com.example.a100pieapp.data.retrofit.MyRetrofit
import com.example.a100pieapp.data.room.CurrencyEntity
import com.example.a100pieapp.data.room.PieDatabase
import kotlinx.coroutines.Deferred
import java.io.IOException
import java.util.concurrent.Callable
import javax.security.auth.callback.Callback

class Repo(var context: Context) {

    private val DATABASE_NAME = "DATABASE_NAME"
    private val piDatabase by lazy {
        Room.databaseBuilder(context.applicationContext, PieDatabase::class.java, DATABASE_NAME)
            .build()
    }
    private val currencyApiService by lazy {
        MyRetrofit.getInstance().create(CurrencyApiService::class.java)
    }

    fun getListFromLocal(): ArrayList<Currencies> {
        var list = ArrayList<Currencies>()
        piDatabase.listDao().getList().forEach {
            list.add(
                Currencies(
                    currency = it.currency,
                    currencyLong = it.currencyLong,
                    txFee = it.txFee
                )
            )
        }

        return list
    }

    fun getItemFromLocalDatabase(index: Int): CurrencyEntity {
        return piDatabase.listDao().getItem(index)
    }

    fun getCurrencyListFromApi(): Deferred<CurrencyHolder> {
        return currencyApiService.getCurrencies()
    }

    fun addListTODatabase(list: ArrayList<CurrencyEntity>) {
        piDatabase.listDao().addListToDatabase(list)
    }

    fun clearTable() {
        piDatabase.listDao().clearTable()
    }

}