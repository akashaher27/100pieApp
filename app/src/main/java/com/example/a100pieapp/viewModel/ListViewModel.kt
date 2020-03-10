package com.example.a100pieapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a100pieapp.data.model.Currencies
import com.example.a100pieapp.data.model.CurrencyHolder
import com.example.a100pieapp.data.room.CurrencyEntity
import com.example.a100pieapp.domain.ListInteractor
import kotlinx.coroutines.*
import java.lang.Exception
import java.sql.Time
import java.time.Duration

class ListViewModel(var context: Context) : ViewModel() {

    private val listInteractor by lazy { ListInteractor(context) }
    private var list: ArrayList<Currencies> = ArrayList()
    private lateinit var item: CurrencyEntity

    val currencyList by lazy {
        MutableLiveData<List<Currencies>>()
    }
    val showProgressBar by lazy {
        MutableLiveData<Boolean>()
    }


    suspend fun populateUI() {
        showProgressBar.value = true
        list.isNotEmpty().let { list.clear() }

        CoroutineScope(Dispatchers.IO).launch {
            list = getCurrencyListFromApi()
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.IO).launch {
                addListToDatabase(list)
            }.invokeOnCompletion {
                CoroutineScope(Dispatchers.IO).launch {
                    getCurrencyListFromLocal()
                }
            }
        }
    }

    suspend fun getCurrencyListFromLocal() {
        try {
            withContext(Dispatchers.IO) {
                list = listInteractor.getListFromLocal()
            }
            withContext(Dispatchers.Main) {
                showProgressBar.value = false
                currencyList.value = list
            }
        } catch (e: Exception) {
            showError()
        }
    }

    suspend fun getCurrencyListFromApi(): ArrayList<Currencies> {
        var list: ArrayList<Currencies> = ArrayList()
        try {
            withContext(Dispatchers.IO) {
                list = listInteractor.getListFromApi().await().result
                clearTable()
            }
        } catch (e: Exception) {
            networksError()
        }
        return list
    }

    suspend fun addListToDatabase(list: ArrayList<Currencies>) {
        try {
            withContext(Dispatchers.IO) {
                list.isNotEmpty().let {
                    listInteractor.addListToDatabase(list)
                }
            }
        } catch (e: Exception) {
            showError()
        }

    }

    suspend fun clearTable() {
        try {
            withContext(Dispatchers.IO) {
                listInteractor.clearTable()
            }
        } catch (e: Exception) {
            showError()
        }
    }

    suspend fun showError() {
        withContext(Dispatchers.Main) {
            showProgressBar.value = false
            Toast.makeText(context, "Something went Wrong ", Toast.LENGTH_LONG).show()
        }
    }

    suspend fun networksError() {
        withContext(Dispatchers.Main) {
            showProgressBar.value = false
            Toast.makeText(context, "please check internet connection", Toast.LENGTH_LONG).show()
        }
    }


    class ListViewModelFactory(var context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(context) as T
        }
    }
}