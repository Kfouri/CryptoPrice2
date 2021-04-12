package com.kfouri.cryptoprice2.domain.usecase

import com.kfouri.cryptoprice2.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyByIdUseCase
@Inject
constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun getCurrencyById(id: Int) = currencyRepository.getCurrencyById(id)
}