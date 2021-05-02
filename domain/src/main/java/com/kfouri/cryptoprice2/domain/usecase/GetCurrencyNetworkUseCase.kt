package com.kfouri.cryptoprice2.domain.usecase

import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyNetworkUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun getCurrencyNetwork(symbol: String) = currencyRepository.getNetworkCurrency(symbol)
}