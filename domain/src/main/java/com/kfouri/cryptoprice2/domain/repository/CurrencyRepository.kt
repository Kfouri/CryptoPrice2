package com.kfouri.cryptoprice2.domain.repository

import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrencyById(id: Int): Flow<DataState<Currency>>
    suspend fun getAllCurrencies(): Flow<DataState<List<Currency>>>
    suspend fun insertUpdateCurrency(currency: Currency)
    suspend fun removeCurrency(currency: Currency)
}