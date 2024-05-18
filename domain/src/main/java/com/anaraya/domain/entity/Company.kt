package com.anaraya.domain.entity

data class Company(
    val data: List<CompanyData>,
    val isSucceed:Boolean,
    val message:String?,
)

data class CompanyData(
    val id:Int,
    val name:String,
)

