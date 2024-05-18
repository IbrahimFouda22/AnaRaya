package com.anaraya.anaraya.home.cart.interaction

interface CartAddressInteraction {
    fun onClickAddress(id: String, position: Int, isUserAddress: Boolean)
}