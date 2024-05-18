package com.anaraya.data.mapper

import com.anaraya.data.dto.RelationshipsDto
import com.anaraya.domain.entity.Relationships

fun RelationshipsDto.toEntity(): List<Relationships> {
    return data.map {
        Relationships(name = it.name, id = it.id)
    }
}