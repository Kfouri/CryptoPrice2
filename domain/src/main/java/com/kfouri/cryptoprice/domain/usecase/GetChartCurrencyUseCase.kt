package com.kfouri.cryptoprice.domain.usecase

import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetChartCurrencyUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun getChartCurrency(symbol: String, days: String) =
        currencyRepository.getChartCurrency(symbol, days)
}