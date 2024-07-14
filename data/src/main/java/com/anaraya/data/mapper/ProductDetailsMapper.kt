package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductDetailsDto
import com.anaraya.domain.entity.Product
import com.anaraya.domain.entity.ProductDetails
import com.anaraya.domain.entity.ProductDetailsData

fun ProductDetailsDto.toEntity(): ProductDetails {
    return ProductDetails(
        data = ProductDetailsData(
            priceBefore = data.pricE_BEFORE,
            priceAfter = data.pricE_AFTER,
            specs2 = data.specs2,
            availableQty = data.availableQty,
            discount = data.discount,
            brand = data.brand,
            mainCat = data.maiN_CAT,
            cat = data.cat,
            id = data.id,
            specs1 = data.specs1,
            description = data.description,
            inBasket = data.inBasket,
            notifyMe = data.notifyMe,
            favouriteStock = data.favouriteStock,
            qtyInBasket = data.qtyInBasket,
            limitQtyForUserPerMonth = data.limityQtyForUserPerMonth,
            qtyUsedFromLimit = data.qtyUsedFromLimtit,
            stockImages = data.stockImages?.map {
                it
            },
            recommendedStocks = data.recomendedStocks.map {
                Product(
                    priceBefore = data.pricE_BEFORE,
                    priceAfter = data.pricE_AFTER,
                    name = data.description,
                    specs2 = data.specs2,
                    availableQty = data.availableQty,
                    discount = data.discount,
                    brand = data.brand,
                    mainCategory = data.maiN_CAT,
                    category = data.cat,
                    id = data.id,
                    specs1 = data.specs1,
                    description = data.description,
                    inBasket = data.inBasket,
                    notifyMe = data.notifyMe,
                    favouriteStock = data.favouriteStock,
                    qtyInBasket = data.qtyInBasket,
                    images = data.stockImages?.map {
                        it
                    },
                    limitQtyForUserPerMonth = data.limityQtyForUserPerMonth,
                    qtyUsedFromLimit = data.qtyUsedFromLimtit,
                )
            },
        )
    )
}

