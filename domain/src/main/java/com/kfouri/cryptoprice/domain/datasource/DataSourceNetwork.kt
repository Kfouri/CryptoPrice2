package com.kfouri.cryptoprice.domain.datasource

interface DataSourceNetwork<C, CANM, CHARTCUR> {
    suspend fun getCurrencies(symb: String): List<C>
    suspend fun getAvailableCurrencyList(): List<CANM>
    suspend fun getCurrencyCoinGecko(symbols: String): List<C>
    suspend fun getChartCurrency(symbol: String, days: String): CHARTCUR
}