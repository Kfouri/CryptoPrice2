package com.kfouri.cryptoprice2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kfouri.cryptoprice2.data.db.dao.CurrencyDao
import com.kfouri.cryptoprice2.data.db.model.CurrencyLocal

@Database(entities = [CurrencyLocal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

}