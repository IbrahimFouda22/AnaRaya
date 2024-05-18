package com.anaraya.data.mapper

import com.anaraya.data.dto.CategoryDto
import com.anaraya.data.dto.MainCategoryDto
import com.anaraya.domain.entity.Category
import com.anaraya.domain.entity.MainCategory

fun MainCategoryDto.toEntity(): List<MainCategory> {
    return data.itemsList.map {
        MainCategory(
            id = it.id,
            name = it.name,
            image = it.image
        )
    }
}


fun CategoryDto.toEntity(): List<Category> {
    return data.map {
        Category(name = it.name, id = it.id)
    }
}