package com.anaraya.data.dto

data class RelationshipsDto(
    val data: List<RelationshipsDtoData>
)

data class RelationshipsDtoData(
    val id:Int,
    val name: String
)
