package com.anaraya.anaraya.home.shared_interaction

interface ProductInteractionListener {
    fun onCLick(productId:Int)
    fun addToCart(productId: Int, position: Int)
}