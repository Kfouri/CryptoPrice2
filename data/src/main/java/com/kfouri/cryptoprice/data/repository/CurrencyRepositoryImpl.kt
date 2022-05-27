package com.kfouri.cryptoprice.data.repository

import com.kfouri.cryptoprice.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import com.kfouri.cryptoprice.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
            emit(DataState.Failure(e))
        }
    }

    override suspend fun getAllCurrencies(): Flow<DataState<List<Currency>>> = flow {
        emit(DataState.InProgress())
        try {
            val list = currencyLocalDataSource.getAllCurrencies()

            if (list.isNotEmpty()) {
                var currenciesId = ""

                list.forEach {
                    it.oldPrice = it.currentPrice
                    currencyLocalDataSource.insertUpdateCurrency(it)
                    currenciesId = currenciesId + it.id + ","
                }

                currenciesId = currenciesId.substring(0, currenciesId.length - 1)

                val currencyList = currencyNetworkDataSource.getCurrencyCoinGecko(currenciesId)

                list.forEach { l ->
                    val c = currencyList.find { l.symbol.compareTo(it.symbol,true) == 0 }
                    l.currentPrice = c?.price?:0.0
                    l.icon = c?.icon?:""
                    l.open24 = c?.open24?:0.0
                    l.name = c?.name?:""
                    l.symbol = c?.symbol?:""

                    currencyLocalDataSource.insertUpdateCurrency(l)
                }
            }

            emit(DataState.Success(list))
        } catch (e: Exception) {
            emit(DataState.Failure(e))
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
            emit(DataState.Failure(e))
        }
    }

    override suspend fun getNetworkCurrency(symbol: String): Flow<DataState<List<CurrencyNetwork>>> = flow {
        emit(DataState.InProgress())
        try {
            val currencyList = currencyNetworkDataSource.getCurrencyCoinGecko(symbol)
            emit(DataState.Success(currencyList))
        } catch (e: Exception) {
            emit(DataState.Failure(e))
        }
    }

    override suspend fun getChartCurrency(symbol: String, days: String): Flow<DataState<ChartCurrency>> = flow {
        emit(DataState.InProgress())
        try {
            val chartCurrency = currencyNetworkDataSource.getChartCurrency(symbol, days)
            emit(DataState.Success(chartCurrency))
        } catch (e: Exception) {
            emit(DataState.Failure(e))
        }
    }
}