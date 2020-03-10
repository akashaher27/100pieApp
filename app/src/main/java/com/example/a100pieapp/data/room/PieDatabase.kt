package com.example.a100pieapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(CurrencyEntity::class), version = 1)
abstract class PieDatabase : RoomDatabase() {

    abstract fun listDao(): ListDao
}