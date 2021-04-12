package com.kfouri.cryptoprice2.domain.usecase

import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import javax.inject.Inject

class InsertUpdateCurrencyUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun insertUpdateCurrency(currency: Currency) = currencyRepository.insertUpdateCurrency(currency)
}