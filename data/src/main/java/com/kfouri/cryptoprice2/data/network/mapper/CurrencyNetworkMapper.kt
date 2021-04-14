package com.kfouri.cryptoprice2.data.network.mapper

import com.kfouri.cryptoprice2.data.network.model.*
import com.kfouri.cryptoprice2.domain.mapper.EntityMapper
import com.kfouri.cryptoprice2.domain.model.CurrencyNetwork
import javax.inject.Inject
class CurrencyNetworkMapper
@Inject
constructor(): EntityMapper<CurrencyNetwork, CurrencyNetworkEntity> {

    override fun toModel(entity: CurrencyNetworkEntity): CurrencyNetwork =
        CurrencyNetwork(entity.name, entity.price, entity.icon)

    override fun toEntity(model: CurrencyNetwork): CurrencyNetworkEntity =
            CurrencyNetworkEntity(model.name, model.price, model.icon)
}

