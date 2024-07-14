package com.anaraya.anaraya.screens.home.interaction

interface ProductPointsInteractionListener {
    fun onCLickPointProduct(productId:Int)
    fun addToCartPointProduct(productId: Int, position: Int)
}