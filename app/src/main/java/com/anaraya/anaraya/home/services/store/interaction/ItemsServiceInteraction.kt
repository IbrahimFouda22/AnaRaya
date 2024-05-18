package com.anaraya.anaraya.home.services.store.interaction

import com.anaraya.anaraya.home.services.store.my_items.ProductStoreItemState

interface ItemsServiceInteraction {
    fun onClick(item: ProductStoreItemState?, itemId: Int, userAction: Int)
}