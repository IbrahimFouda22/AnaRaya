package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductDto
import com.anaraya.domain.entity.Product

fun ProductDto.toEntity(): List<Product> {
    return data.itemsList.map {
        Product(
            priceBefore = it.pricE_BEFORE,
            priceAfter = it.pricE_AFTER,
            name = it.description,
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
            inLoyaltyBasket = it.isInLoyaltyBasket,
            textTrending = message ?: ""
        )
    }
}
