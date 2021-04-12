package com.kfouri.cryptoprice2.domain.datasource

interface DataSourceNetwork<C> {
    suspend fun getCurrency(name: String): C
}