package com.anaraya.data.dto

data class CategoryDto(
    val data: List<CategoryDtoData>
)

data class CategoryDtoData(
    val id:Int,
    val name: String
)
