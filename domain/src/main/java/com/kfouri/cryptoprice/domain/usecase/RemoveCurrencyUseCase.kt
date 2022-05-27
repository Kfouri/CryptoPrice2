package com.kfouri.cryptoprice.domain.usecase

import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import javax.inject.Inject

class RemoveCurrencyUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun removeCurrency(currency: Currency) = currencyRepository.removeCurrency(currency)
}