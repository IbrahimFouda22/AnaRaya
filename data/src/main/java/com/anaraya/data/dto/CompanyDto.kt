package com.anaraya.data.dto

data class CompanyDto(
    val data: List<CompanyDtoData>,
    val isSucceed:Boolean,
    val message:String?,
)

data class CompanyDtoData(
    val id:Int,
    val name:String,
)

