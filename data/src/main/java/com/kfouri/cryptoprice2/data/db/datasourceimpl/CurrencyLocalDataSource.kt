package com.kfouri.cryptoprice2.data.db.datasourceimpl

import com.kfouri.cryptoprice2.data.db.dao.CurrencyDao
import com.kfouri.cryptoprice2.data.db.mapper.CurrencyLocalMapper
import com.kfouri.cryptoprice2.domain.datasource.DataSource
import com.kfouri.cryptoprice2.domain.model.Currency
import javax.inject.Inject

class CurrencyLocalDataSource
@Inject
constructor(
    private val currencyDao: CurrencyDao,
    private val currencyLocalMapper: CurrencyLocalMapper
): DataSource<Currency> {
    override suspend fun getCurrency(id: Int): Currency {
        val currencyLocal = currencyDao.getCurrency(id)
        return currencyLocalMapper.toModel(currencyLocal)
    }

    override suspend fun getAllCurrencies(): List<Currency> {
        val list = currencyDao.getAllCurrencies()
        return currencyLocalMapper.toModelList(list)
    }

    override suspend fun insertUpdateCurrency(currency: Currency) {
        val currencyLocal = currencyLocalMapper.toEntity(currency)
        currencyDao.insertUpdateCurrency(currencyLocal)
    }

    override suspend fun removeCurrency(currency: Currency) {
        val currencyLocal = currencyLocalMapper.toEntity(currency)
        currencyDao.removeCurrency(currencyLocal)
    }

}