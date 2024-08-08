package com.anaraya.anaraya.screens.shared_data

import java.io.Serializable

data class AddOrderUiState(
    val subTotal: Double = 0.0,
    val total: Double = 0.0,
    val fee: Double = 0.0,
    val shipping: Double = 0.0,
    val discount: Double = 0.0,
    val promoCode: String = "",
    val numOfItems: Int = 0,
    val amountToTakeDeliveryFree: Double = 0.0,
) : Serializable
