package com.anaraya.data.mapper

import com.anaraya.data.dto.ExploreProductsDto
import com.anaraya.domain.entity.ExploreProduct

fun ExploreProductsDto.toEntity(): List<ExploreProduct> {
    return data.itemsList.map {
        ExploreProduct(
            category = it.category,
            id = it.id,
            subCategory = it.subCategory,
            itemDescription = it.itemDescription,
            isUserProduct = it.isUserProduct,
            title = it.title,
            productImageUrl = it.productImageUrl,
            condition = it.condition,
            sellingStatus = it.sellingStatus ?: 0,
            price = it.price,
            location = it.location,
            sellerName = it.sellerName ?: "",
            sellerPhoneNumber = it.sellerPhoneNumber ?: ""
        )
    }
}
