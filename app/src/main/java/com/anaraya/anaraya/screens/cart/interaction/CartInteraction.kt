package com.anaraya.anaraya.screens.cart.interaction

interface CartInteraction {
    fun onClickDelete(productId: Int, position: Int, isLoyalty: Boolean)
    fun onClickPlus(productId: Int, newQty: Int, isLoyalty: Boolean)
    fun onClickMinus(productId: Int, newQty: Int, isLoyalty: Boolean)
}