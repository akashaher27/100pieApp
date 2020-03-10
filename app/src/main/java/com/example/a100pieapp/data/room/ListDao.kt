package com.example.a100pieapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.Deferred


@Dao
interface ListDao {

    @Query("SELECT * FROM currency")
    fun getList(): List<CurrencyEntity>

    @Insert
    fun addListToDatabase(list: ArrayList<CurrencyEntity>)

    @Query("DELETE FROM currency")
    fun clearTable()

    @Query("SELECT * FROM currency WHERE uid = :index")
    fun getItem(index: Int): CurrencyEntity
}