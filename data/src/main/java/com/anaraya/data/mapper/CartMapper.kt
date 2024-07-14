package com.anaraya.data.mapper

import com.anaraya.data.dto.CartDto
import com.anaraya.data.dto.CartDtoData
import com.anaraya.domain.entity.Cart
import com.anaraya.domain.entity.CartData
import com.anaraya.domain.entity.CartDataList

fun CartDto.toEntity():Cart{
    return Cart(
        isSucceed = isSucceed,
        message = message,
        data = data.toEntity(),
    )
}

fun CartDtoData.toEntity():CartData{
    return CartData(
        cartTotal = this.cartTotal,
        cartTotalAmount = this.totalAmount,
        cartDeliveryFee = this.deliveryFee,
        cartAmountToTakeFreeDelivery = this.ammountToTakeFreeDelivery,
        cartPromoCodeDiscount = this.promoCodeDiscount,
        hasAddress = this.hasAddress,
        data = this.stocksList.map {
            CartDataList(
                id = it.id,
                pricePerOne = it.pricePerOne,
                productAvailableQty = it.productAvailableQty,
                totalProductPrice = it.totalPrice,
                qty = it.qty,
                specs1 = it.specs1,
                specs2 = it.specs2,
                stockImg = it.stockImg,
                deliveryFee = it.deliveryFee,
                description = it.description,
            )
        },
        loyaltyData = this.loyaltyStockList.map {
            CartDataList(
                id = it.id,
                pricePerOne = it.pointsPerOne,
                productAvailableQty = it.productAvailableQty,
                totalProductPrice = it.totalPoints,
                qty = it.qty,
                specs1 = it.specs1,
                specs2 = it.specs2,
                stockImg = it.stockImg,
                description = it.description,
                deliveryFee = 0.0,
            )
        }
    )
}