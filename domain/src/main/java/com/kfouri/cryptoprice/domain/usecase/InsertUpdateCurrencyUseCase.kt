package com.kfouri.cryptoprice.domain.usecase

import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import javax.inject.Inject

class InsertUpdateCurrencyUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun insertUpdateCurrency(currency: Currency) = currencyRepository.insertUpdateCurrency(currency)
}