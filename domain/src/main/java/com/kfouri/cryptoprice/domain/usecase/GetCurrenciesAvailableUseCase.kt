package com.kfouri.cryptoprice.domain.usecase

import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrenciesAvailableUseCase
@Inject
constructor(
        private val currencyRepository: CurrencyRepository
) {
    suspend fun getCurrenciesAvailable() = currencyRepository.getCurrenciesAvailable()
}