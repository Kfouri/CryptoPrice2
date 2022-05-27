package com.kfouri.cryptoprice.data.network.mapper

import com.kfouri.cryptoprice.data.network.model.ChartCurrencyEntity
import com.kfouri.cryptoprice.domain.mapper.EntityMapper
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import javax.inject.Inject

class ChartCurrencyMapper
@Inject
constructor(): EntityMapper<ChartCurrency, ChartCurrencyEntity> {

    override fun toModel(entity: ChartCurrencyEntity): ChartCurrency =
        ChartCurrency(entity.prices, entity.marketCaps, entity.totalVolumes)

    override fun toEntity(model: ChartCurrency): ChartCurrencyEntity =
        ChartCurrencyEntity(model.prices, model.marketCaps, model.totalVolumes)
}