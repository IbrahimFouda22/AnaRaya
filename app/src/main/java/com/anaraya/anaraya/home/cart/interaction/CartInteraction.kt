package com.anaraya.anaraya.home.cart.interaction

interface CartInteraction {
    fun onClickDelete(productId: Int, position: Int)
    fun onClickPlus(productId: Int, newQty: Int)
    fun onClickMinus(productId: Int, newQty: Int)
}