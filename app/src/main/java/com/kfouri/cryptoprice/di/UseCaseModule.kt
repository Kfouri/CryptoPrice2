package com.kfouri.cryptoprice.di

import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import com.kfouri.cryptoprice.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetAllCurrenciesUseCase(
        currencyRepository: CurrencyRepository
    ): GetAllCurrenciesUseCase {
        return GetAllCurrenciesUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrencyByIdUseCase(
        currencyRepository: CurrencyRepository
    ): GetCurrencyByIdUseCase {
        return GetCurrencyByIdUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideInsertUpdateCurrencyUseCase(
        currencyRepository: CurrencyRepository
    ): InsertUpdateCurrencyUseCase {
        return InsertUpdateCurrencyUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideRemoveCurrencyUseCase(
        currencyRepository: CurrencyRepository
    ): RemoveCurrencyUseCase {
        return RemoveCurrencyUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrenciesAvailableUseCase(
            currencyRepository: CurrencyRepository
    ): GetCurrenciesAvailableUseCase {
        return GetCurrenciesAvailableUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrencyNetworkUseCase(
            currencyRepository: CurrencyRepository
    ): GetCurrencyNetworkUseCase {
        return GetCurrencyNetworkUseCase(currencyRepository)
    }

    @Singleton
    @Provides
    fun provideGetChartCurrencyUseCase(
        currencyRepository: CurrencyRepository
    ): GetChartCurrencyUseCase {
        return GetChartCurrencyUseCase(currencyRepository)
    }
}