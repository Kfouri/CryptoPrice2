package com.kfouri.cryptoprice2.domain.datasource

interface DataSourceNetwork<C, CANM> {
    suspend fun getCurrency(name: String): C
    suspend fun getAvailableCurrencyList(): List<CANM>
}