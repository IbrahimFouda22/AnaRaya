package com.anaraya.anaraya.home.more.family

import com.anaraya.domain.entity.Relationships

data class FamilyUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameError: Boolean = false,
    val phoneError: Boolean = false,
    val relationshipError: Boolean = false,
    val emailErrorMsg: String = "",
    val addReferralMsg: String? = null,
    val listRelationships: List<RelationshipsUiState> = emptyList()
)

data class RelationshipsUiState(
    val id: Int,
    val name: String
)

fun Relationships.toUiState() = RelationshipsUiState(
    id = id,
    name = name
)