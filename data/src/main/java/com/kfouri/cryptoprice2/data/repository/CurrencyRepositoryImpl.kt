package com.kfouri.cryptoprice2.data.repository

import android.util.Log
import com.kfouri.cryptoprice2.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice2.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import com.kfouri.cryptoprice2.domain.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
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

            if (list.isNotEmpty()) {
                var currencyNames = ""

                list.forEach {
                    it.oldPrice = it.currentPrice
                    currencyLocalDataSource.insertUpdateCurrency(it)
                    currencyNames = currencyNames + it.symbol + ","
                }

                currencyNames = currencyNames.substring(0, currencyNames.length - 1)

                val currencyList = currencyNetworkDataSource.getCurrencies(currencyNames)

                list.forEach { l ->
                    val c = currencyList.find { l.symbol.compareTo(it.name,true) == 0 }
                    l.currentPrice = c?.price?:0.0
                    l.icon = c?.icon?:""
                    l.open24 = c?.open24?:0.0

                    currencyLocalDataSource.insertUpdateCurrency(l)
                }
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

    override suspend fun getNetworkCurrency(symbol: String): Flow<DataState<List<CurrencyNetwork>>> = flow {
        emit(DataState.InProgress())
        try {
            val currencyList = currencyNetworkDataSource.getCurrencies(symbol)
            emit(DataState.Success(currencyList))
        } catch (e: Exception) {
            emit(DataState.Failure<List<CurrencyNetwork>>(e))
        }
    }
}