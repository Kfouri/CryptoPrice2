package com.kfouri.cryptoprice2.data.db.dao

import androidx.room.*
import com.kfouri.cryptoprice2.data.db.model.CurrencyLocal

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency where _id = :id")
    suspend fun getCurrency(id: Int): CurrencyLocal

    @Query("SELECT * FROM currency")
    suspend fun getAllCurrencies(): List<CurrencyLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateCurrency(currencyLocal: CurrencyLocal)

    @Delete
    suspend fun removeCurrency(currencyLocal: CurrencyLocal)
}