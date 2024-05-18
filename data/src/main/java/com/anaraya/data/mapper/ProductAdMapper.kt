package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductsAdsDto
import com.anaraya.domain.entity.ProductAd

fun ProductsAdsDto.toEntity():List<ProductAd>{
    return data.map {
        ProductAd(
            id = it.id,
            image = it.adsImageUrl,
            isProductOrBrand = it.isProductOrBrand
        )
    }
}
