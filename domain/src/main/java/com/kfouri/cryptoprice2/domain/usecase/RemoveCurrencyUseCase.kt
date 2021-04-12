package com.kfouri.cryptoprice2.domain.usecase

import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import javax.inject.Inject

class RemoveCurrencyUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun removeCurrency(currency: Currency) = currencyRepository.removeCurrency(currency)
}