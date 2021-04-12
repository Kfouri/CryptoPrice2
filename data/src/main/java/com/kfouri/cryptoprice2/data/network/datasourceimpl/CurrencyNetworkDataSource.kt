package com.kfouri.cryptoprice2.data.network.datasourceimpl

import com.kfouri.cryptoprice2.data.network.mapper.CurrencyNetworkMapper
import com.kfouri.cryptoprice2.data.network.service.CurrencyService
import com.kfouri.cryptoprice2.domain.datasource.DataSourceNetwork
import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork
import javax.inject.Inject

class CurrencyNetworkDataSource
@Inject
constructor(
        private val currencyService: CurrencyService,
        private val currencyNetworkMapper: CurrencyNetworkMapper
)
    : DataSourceNetwork<CurrencyNetwork> {

    override suspend fun getCurrency(name: String): CurrencyNetwork {
        return currencyNetworkMapper.toModel(currencyService.getPrice(name))
    }
}