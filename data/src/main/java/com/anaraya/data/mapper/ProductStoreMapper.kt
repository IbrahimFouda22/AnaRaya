package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductStoreDto
import com.anaraya.data.dto.ProductStoreDtoDetails
import com.anaraya.data.dto.ProductStoreDtoItemList
import com.anaraya.domain.entity.CustomerInformation
import com.anaraya.domain.entity.ProductStore

fun ProductStoreDto.toEntity(): List<ProductStore> {
    return data.itemsList.map {
        it.toEntity()
    }
}

fun ProductStoreDtoItemList.toEntity(): ProductStore {
    return ProductStore(
        id = id,
        category = category,
        categoryId = categoryId,
        subCategory = subCategory,
        subCategoryId = subCategoryId,
        condition = condition,
        title = title,
        itemDescription = itemDescription,
        price = price,
        location = location,
        productImageUrl = productImageUrl,
        userAction = userAction,
        status = status,
        isAnonymous = isAnonymous ?: false,
        handleDelivery = handleDelivery ?: false,
        customerInformation = customerInformation?.let {
            CustomerInformation(
                listeningId = customerInformation.listiningId,
                id = customerInformation.id,
                name = customerInformation.name,
                nationalID = customerInformation.nationalID,
                hrId = customerInformation.hrid,
                phoneNumber = customerInformation.phoneNumber,
                email = customerInformation.email,
                payMethod = customerInformation.payMethod,
                sellingStatus = customerInformation.sellingStatus
            )
        },
        customerWantsToBuy = customerWantsToBuy
    )
}

fun ProductStoreDtoDetails.toEntity(): ProductStore {
    return data.toEntity()
}