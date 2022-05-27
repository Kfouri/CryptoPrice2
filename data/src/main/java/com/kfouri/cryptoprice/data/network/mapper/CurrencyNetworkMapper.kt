package com.kfouri.cryptoprice.data.network.mapper

import com.kfouri.cryptoprice.data.network.model.*
import com.kfouri.cryptoprice.domain.mapper.EntityMapper
import com.kfouri.cryptoprice.domain.model.CurrencyNetwork
import javax.inject.Inject
class CurrencyNetworkMapper
@Inject
constructor(): EntityMapper<CurrencyNetwork, CurrencyNetworkEntity> {

    override fun toModel(entity: CurrencyNetworkEntity): CurrencyNetwork =
        CurrencyNetwork(
            entity.name,
            entity.symbol,
            entity.price,
            entity.icon,
            entity.open24,
            entity.marketCapRank,
            entity.high24h,
            entity.low24h
        )

    override fun toEntity(model: CurrencyNetwork): CurrencyNetworkEntity =
            CurrencyNetworkEntity(
                model.name,
                model.symbol,
                model.price,
                model.icon,
                model.open24,
                model.marketCapRank,
                model.high24h,
                model.low24h
            )
}

