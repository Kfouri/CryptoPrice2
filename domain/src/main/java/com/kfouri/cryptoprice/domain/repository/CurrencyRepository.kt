package com.kfouri.cryptoprice.domain.repository

import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice.domain.state.DataState
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getCurrencyById(id: Int): Flow<DataState<Currency>>
    suspend fun getAllCurrencies(): Flow<DataState<List<Currency>>>
    suspend fun insertUpdateCurrency(currency: Currency)
    suspend fun removeCurrency(currency: Currency)
    suspend fun getCurrenciesAvailable(): Flow<DataState<List<CurrencyAvailableNetwork>>>
    suspend fun getNetworkCurrency(symbol: String): Flow<DataState<List<CurrencyNetwork>>>
    suspend fun getChartCurrency(symbol: String, days: String): Flow<DataState<ChartCurrency>>
}