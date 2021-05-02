package com.kfouri.cryptoprice2.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kfouri.cryptoprice2.data.db.model.CurrencyLocal.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CurrencyLocal(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "exchange") val exchange: String = "",
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "purchasePrice") val purchasePrice: Double = 0.0,
    @ColumnInfo(name = "currentPrice") var currentPrice: Double = 0.0,
    @ColumnInfo(name = "oldPrice") var oldPrice: Double = 0.0,
    @ColumnInfo(name = "icon") var icon: String,
) {
    companion object {
        const val TABLE_NAME = "Currency"
    }
}
