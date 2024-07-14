package com.anaraya.anaraya.screens.services.store.interaction

import com.anaraya.anaraya.screens.services.store.service.my_items.ProductStoreItemServiceState

interface ItemsServicesInteraction {
    fun onClick(item: ProductStoreItemServiceState?, itemId: Int, userAction: Int)
}