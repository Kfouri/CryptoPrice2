package com.kfouri.cryptoprice2.di

import com.kfouri.cryptoprice2.data.db.dao.CurrencyDao
import com.kfouri.cryptoprice2.data.db.datasourceimpl.CurrencyLocalDataSource
import com.kfouri.cryptoprice2.data.db.mapper.CurrencyLocalMapper
import com.kfouri.cryptoprice2.data.network.datasourceimpl.CurrencyNetworkDataSource
import com.kfouri.cryptoprice2.data.network.mapper.CurrencyNetworkMapper
import com.kfouri.cryptoprice2.data.network.service.CurrencyService
import com.kfouri.cryptoprice2.data.repository.CurrencyRepositoryImpl
import com.kfouri.cryptoprice2.domain.datasource.DataSource
import com.kfouri.cryptoprice2.domain.datasource.DataSourceNetwork
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
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
            currencyNetworkMapper: CurrencyNetworkMapper): DataSourceNetwork<CurrencyNetwork> {
        return CurrencyNetworkDataSource(currencyService, currencyNetworkMapper)
    }

}