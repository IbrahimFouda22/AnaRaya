package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductStoreDto
import com.anaraya.data.dto.ProductStoreDtoDetails
import com.anaraya.data.dto.ProductStoreDtoDetailsForCustomer
import com.anaraya.data.dto.ProductStoreDtoItemList
import com.anaraya.domain.entity.CustomerInformation
import com.anaraya.domain.entity.ProductStore
import com.anaraya.domain.entity.ProductStoreForCustomer

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
                email = customerInformation.email ?: "",
                payMethod = customerInformation.payMethod ?: -1,
                sellingStatus = customerInformation.sellingStatus ?: 0
            )
        },
        customerWantsToBuy = customerWantsToBuy,
        isPaymentConfirmed = isPaymentConfirmed ?: false
    )
}

fun ProductStoreDtoDetails.toEntity(): ProductStore {
    return data.toEntity()
}

fun ProductStoreDtoDetailsForCustomer.toEntity(): ProductStoreForCustomer {
    return ProductStoreForCustomer(
        id = data.id,
        category = data.category,
        subCategory = data.subCategory,
        condition = data.condition,
        title = data.title,
        itemDescription = data.itemDescription,
        price = data.price,
        location = data.location,
        productImageUrl = data.productImageUrl,
        isAnonymous = data.isAnonymous,
        sellerName = data.sellerName ?: "",
        sellingStatus = data.sellingStatus ?: 0,
        sellerPhoneNumber = data.sellerPhoneNumber ?: "",
        isUserProduct = data.isUserProduct
    )
}