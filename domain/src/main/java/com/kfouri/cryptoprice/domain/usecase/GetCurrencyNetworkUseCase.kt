package com.kfouri.cryptoprice.domain.usecase

import com.kfouri.cryptoprice.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyNetworkUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun getCurrencyNetwork(symbol: String) = currencyRepository.getNetworkCurrency(symbol)
}