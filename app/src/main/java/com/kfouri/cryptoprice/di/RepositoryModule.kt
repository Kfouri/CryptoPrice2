package com.kfouri.cryptoprice.di

import com.kfouri.cryptoprice.data.db.dao.CurrencyDao
import com.kfouri.cryptoprice.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice.data.db.mapper.CurrencyLocalMapper
import com.kfouri.cryptoprice.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice.data.network.mapper.ChartCurrencyMapper
import com.kfouri.cryptoprice.data.network.mapper.CurrencyAvailableNetworkMapper
import com.kfouri.cryptoprice.data.network.mapper.CurrencyNetworkMapper
import com.kfouri.cryptoprice.data.network.service.CurrencyService
import com.kfouri.cryptoprice.data.repository.CurrencyRepositoryImpl
import com.kfouri.cryptoprice.domain.datasource.DataSource
import com.kfouri.cryptoprice.domain.datasource.DataSourceNetwork
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork
import com.kfouri.cryptoprice.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCurrencyLocalDataSource(
        currencyDao: CurrencyDao,
        currencyLocalMapper: CurrencyLocalMapper): DataSource<Currency> {
        return CurrencyLocalDataSource(currencyDao, currencyLocalMapper)
    }

    @Singleton
    @Provides
    fun provideProductRepository(
        currencyLocalDataSource: CurrencyLocalDataSource,
        currencyNetworkDataSource: CurrencyNetworkDataSource
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyLocalDataSource, currencyNetworkDataSource)
    }

    @Singleton
    @Provides
    fun provideCurrencyNetworkDataSource(
            currencyService: CurrencyService,
            currencyNetworkMapper: CurrencyNetworkMapper,
            currencyAvailableNetworkMapper: CurrencyAvailableNetworkMapper,
            chartCurrencyMapper: ChartCurrencyMapper
    ): DataSourceNetwork<CurrencyNetwork, CurrencyAvailableNetwork, ChartCurrency> {
        return CurrencyNetworkDataSource(currencyService, currencyNetworkMapper, currencyAvailableNetworkMapper, chartCurrencyMapper)
    }

}