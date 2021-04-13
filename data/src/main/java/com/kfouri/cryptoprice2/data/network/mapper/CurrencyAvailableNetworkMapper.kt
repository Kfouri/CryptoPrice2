package com.kfouri.cryptoprice2.data.network.mapper

import com.kfouri.cryptoprice2.data.network.model.*
import com.kfouri.cryptoprice2.domain.mapper.EntityMapper
import com.kfouri.cryptoprice2.domain.model.CurrencyAvailableNetwork
import javax.inject.Inject

class CurrencyAvailableNetworkMapper
@Inject
constructor(): EntityMapper<CurrencyAvailableNetwork, CurrencyAvailableNetworkEntity> {

    override fun toModel(entity: CurrencyAvailableNetworkEntity): CurrencyAvailableNetwork =
            CurrencyAvailableNetwork(entity.id, entity.name, entity.symbol)

    override fun toEntity(model: CurrencyAvailableNetwork): CurrencyAvailableNetworkEntity =
            CurrencyAvailableNetworkEntity(model.id, model.name, model.symbol)
}