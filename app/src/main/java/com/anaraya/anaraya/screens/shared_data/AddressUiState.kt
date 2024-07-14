package com.anaraya.anaraya.screens.shared_data

import com.anaraya.domain.entity.Address
import java.io.Serializable

data class AddressUiState(
    val id: String,
    val addressLabel: String,
    val governorate: String,
    val district: String,
    val address: String,
    val street: String,
    val building: String,
    val landmark: String,
): Serializable

fun Address.toUiState():AddressUiState{
    return AddressUiState(
        id = id,
        addressLabel = addressLabel,
        governorate = governorate,
        district = district,
        address = address,
        street = street,
        building = building,
        landmark = landmark
    )
}