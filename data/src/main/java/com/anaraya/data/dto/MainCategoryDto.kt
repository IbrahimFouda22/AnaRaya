package com.anaraya.data.dto

data class MainCategoryDto(
    val data: MainCategoryDtoData
)

data class MainCategoryDtoData(
    val itemsList:List<MainCategoryDtoItemList>
)

data class MainCategoryDtoItemList(
    val id:Int,
    val name: String,
    val image: String?=null,
)
