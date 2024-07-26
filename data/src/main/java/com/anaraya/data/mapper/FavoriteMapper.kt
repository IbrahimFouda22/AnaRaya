package com.anaraya.data.mapper

import com.anaraya.data.dto.FavoriteDto
import com.anaraya.domain.entity.Product

fun FavoriteDto.toEntity(): List<Product> {
    return data.map {
        Product(
            priceBefore = it.pricE_BEFORE,
            priceAfter = it.pricE_AFTER,
            name = it.maiN_CAT,
            specs1 = it.specs1,
            specs2 = it.specs2,
            availableQty = it.availableQty,
            discount = it.discount,
            image = it.stockImg,
            brand = it.brand,
            mainCategory = it.maiN_CAT,
            category = it.cat,
            id = it.id,
            description = it.description,
            inBasket = it.isInBasket,
            limitQtyForUserPerMonth = it.limitQtyForUserPerMonth,
            qtyUsedFromLimit = it.qtyUsedFromLimit,
            notifyMe = it.notifyMe,
            pointInRedeem = it.pointInRedem ?: 0.0
        )
    }
}
