package com.anaraya.data.mapper

import com.anaraya.data.dto.ExploreServicesDto
import com.anaraya.domain.entity.ExploreServices

fun ExploreServicesDto.toEntity(): List<ExploreServices> {
    return data.itemsList.map {
        ExploreServices(
            category = it.category,
            id = it.id,
            subCategory = it.subCategory,
            serviceDescription = it.serviceDescription,
            isUserService = it.isUserService,
            title = it.title,
            serviceImageUrl = it.serviceImageUrl,
            rentStatus = it.rentStatus ?: -1,
            rentTo = it.rentTo ?: "",
            rentFrom = it.rentFrom ?: "",
            price = it.price,
            location = it.location,
        )
    }
}
