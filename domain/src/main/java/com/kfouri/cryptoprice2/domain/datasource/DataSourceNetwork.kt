package com.kfouri.cryptoprice2.domain.datasource

interface DataSourceNetwork<C, CANM> {
    suspend fun getCurrencies(symbols: String): List<C>
    suspend fun getAvailableCurrencyList(): List<CANM>
}