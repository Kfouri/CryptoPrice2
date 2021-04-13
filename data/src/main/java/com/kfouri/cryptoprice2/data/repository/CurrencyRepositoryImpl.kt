package com.kfouri.cryptoprice2.data.repository

import com.kfouri.cryptoprice2.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice2.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import com.kfouri.cryptoprice2.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CurrencyRepositoryImpl
@Inject
constructor(
        private val currencyLocalDataSource: CurrencyLocalDataSource,
        private val currencyNetworkDataSource: CurrencyNetworkDataSource,
): CurrencyRepository {

    override suspend fun getCurrencyById(id: Int): Flow<DataState<Currency>> = flow {
        emit(DataState.InProgress())
        try {
            val currency = currencyLocalDataSource.getCurrency(id)
            emit(DataState.Success(currency))
        } catch (e: Exception) {
            emit(DataState.Failure<Currency>(e))
        }
    }

    override suspend fun getAllCurrencies(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.InProgress())
        try {
            val list = currencyLocalDataSource.getAllCurrencies()

            list.forEach {
                it.oldPrice = it.currentPrice
                runBlocking {
                    it.currentPrice = currencyNetworkDataSource.getCurrency(it.name).price
                    it.icon = currencyNetworkDataSource.getCurrency(it.name).icon
                }
                currencyLocalDataSource.insertUpdateCurrency(it)
            }

            emit(DataState.Success(list))
        } catch (e: Exception) {
            emit(DataState.Failure<List<Currency>>(e))
        }
    }

    override suspend fun insertUpdateCurrency(currency: Currency) {
        currencyLocalDataSource.insertUpdateCurrency(currency)
        currencyLocalDataSource.getAllCurrencies()
    }

    override suspend fun removeCurrency(currency: Currency) {
        currencyLocalDataSource.removeCurrency(currency)
        currencyLocalDataSource.getAllCurrencies()
    }

    override suspend fun getCurrenciesAvailable(): Flow<DataState<List<CurrencyAvailableNetwork>>> = flow {
        emit(DataState.InProgress())
        try {
            val list = currencyNetworkDataSource.getAvailableCurrencyList()
            emit(DataState.Success(list))
        } catch (e: Exception) {
            emit(DataState.Failure<List<CurrencyAvailableNetwork>>(e))
        }
    }

}