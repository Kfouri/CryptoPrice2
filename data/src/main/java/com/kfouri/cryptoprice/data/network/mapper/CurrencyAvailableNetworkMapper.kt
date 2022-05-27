package com.kfouri.cryptoprice.data.network.mapper

import com.kfouri.cryptoprice.data.network.model.*
import com.kfouri.cryptoprice.domain.mapper.EntityMapper
import com.kfouri.cryptoprice.domain.model.CurrencyAvailableNetwork
import javax.inject.Inject

class CurrencyAvailableNetworkMapper
@Inject
constructor(): EntityMapper<CurrencyAvailableNetwork, CurrencyAvailableNetworkEntity> {

    override fun toModel(entity: CurrencyAvailableNetworkEntity): CurrencyAvailableNetwork =
            CurrencyAvailableNetwork(entity.id, entity.name, entity.symbol)

    override fun toEntity(model: CurrencyAvailableNetwork): CurrencyAvailableNetworkEntity =
            CurrencyAvailableNetworkEntity(model.id, model.name, model.symbol)
}