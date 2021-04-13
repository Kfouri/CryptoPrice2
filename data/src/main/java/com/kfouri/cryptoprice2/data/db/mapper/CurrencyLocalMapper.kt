package com.kfouri.cryptoprice2.data.db.mapper

import com.kfouri.cryptoprice2.data.db.model.CurrencyLocal
import com.kfouri.cryptoprice2.domain.mapper.EntityMapper
import com.kfouri.cryptoprice2.domain.model.Currency
import javax.inject.Inject

class CurrencyLocalMapper
@Inject
constructor(): EntityMapper<Currency, CurrencyLocal> {

    override fun toModel(entity: CurrencyLocal): Currency =
        Currency(
            id = entity.id,
            name = entity.name,
            exchange = entity.exchange,
            amount = entity.amount,
            purchasePrice = entity.purchasePrice,
            currentPrice = entity.currentPrice,
            oldPrice = entity.oldPrice,
            icon = entity.icon
        )

    override fun toEntity(model: Currency): CurrencyLocal =
        CurrencyLocal(
                id = model.id,
                name = model.name,
                exchange = model.exchange,
                amount = model.amount,
                purchasePrice = model.purchasePrice,
                currentPrice = model.currentPrice,
                oldPrice = model.oldPrice,
                icon = model.icon
        )
}