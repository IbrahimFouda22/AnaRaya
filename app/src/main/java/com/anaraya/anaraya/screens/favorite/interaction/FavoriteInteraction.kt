package com.anaraya.anaraya.screens.favorite.interaction

interface FavoriteInteraction {
    fun onClickDelete(productId: Int, position: Int)
    fun onClickCheckBox(productId: Int, isChecked: Boolean)
}