package com.example.a100pieapp.domain

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.a100pieapp.data.Repo
import com.example.a100pieapp.data.model.Currencies
import com.example.a100pieapp.data.model.CurrencyHolder
import com.example.a100pieapp.data.room.CurrencyEntity
import kotlinx.coroutines.Deferred
import java.lang.Exception

class ListInteractor(var context: Context) {

    private val repo by lazy { Repo(context) }


    fun getListFromLocal(): ArrayList<Currencies> {
        return repo.getListFromLocal()
    }

    fun getItem(index: Int): CurrencyEntity {
        return repo.getItemFromLocalDatabase(index)
    }

    fun getListFromApi(): Deferred<CurrencyHolder> {
        return repo.getCurrencyListFromApi()
    }

    fun addListToDatabase(list: ArrayList<Currencies>) {
        val tempList = ArrayList<CurrencyEntity>()
        var counter = 0
        list.map {
            counter++
            tempList.add(
                CurrencyEntity(
                    counter,
                    currency = it.currency,
                    currencyLong = it.currencyLong,
                    txFee = it.txFee
                )
            )
        }
        repo.addListTODatabase(tempList)
    }

    fun clearTable() {
        repo.clearTable()
    }
}
