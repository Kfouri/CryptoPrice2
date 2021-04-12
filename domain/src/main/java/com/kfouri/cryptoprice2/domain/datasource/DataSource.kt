package com.kfouri.cryptoprice2.domain.datasource

interface DataSource<C> {

    suspend fun getCurrency(id: Int): C
    suspend fun getAllCurrencies(): List<C>
    suspend fun insertUpdateCurrency(currency: C)
    suspend fun removeCurrency(currency: C)

}