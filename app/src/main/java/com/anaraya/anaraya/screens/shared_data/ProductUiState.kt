package com.anaraya.anaraya.screens.shared_data

data class ProductUiState(
    val image: String? = null,
    val name: String,
    val price: Double,
    val discount: Double,
    val priceBeforeDiscount: Double,
    val qty: Int,
    val availableQty: Int,
    var inBasket: Boolean,
    val catName: String,
    val specs1: String,
    val specs2: String,
    val id: Int,
    val limitQtyForUserPerMonth: Int,
    val qtyUsedFromLimit: Int,
    val isPoints: Boolean = false,
    val textTrending: String? = null,
){
    var isSelectedInFavToAddToCart:Boolean = false
}
