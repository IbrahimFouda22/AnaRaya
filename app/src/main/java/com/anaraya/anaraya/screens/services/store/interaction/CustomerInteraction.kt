package com.anaraya.anaraya.screens.services.store.interaction

interface CustomerInteraction {
    fun onClickCustomer(listeningId: Int, position: Int)
    fun onClickNumberValue(text: String)
}