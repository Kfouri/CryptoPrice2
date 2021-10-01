package com.kfouri.cryptoprice2.domain.datasource

import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork

interface DataSourceNetwork<C, CANM> {
    suspend fun getCurrencies(symbols: String): List<C>
    suspend fun getAvailableCurrencyList(): List<CANM>
    suspend fun getCurrencyCoinGecko(symbols: String): List<C>
}