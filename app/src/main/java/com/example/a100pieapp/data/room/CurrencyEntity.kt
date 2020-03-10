package com.example.a100pieapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(

    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: Int,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "currency_long")
    val currencyLong: String,
    @ColumnInfo(name = "tx_fee")
    val txFee: String
)