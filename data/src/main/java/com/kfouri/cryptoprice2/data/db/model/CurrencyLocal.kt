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
    @ColumnInfo(name = "amount") val amount: Float,
    @ColumnInfo(name = "purchasePrice") val purchasePrice: Float = 0F,
    @ColumnInfo(name = "currentPrice") var currentPrice: Float = 0F,
    @ColumnInfo(name = "oldPrice") var oldPrice: Float = 0F,
    @ColumnInfo(name = "icon") var icon: String,
) {
    companion object {
        const val TABLE_NAME = "Currency"
    }
}
