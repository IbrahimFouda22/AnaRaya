package com.anaraya.data.mapper

import com.anaraya.data.dto.ProductStoreDto
import com.anaraya.domain.entity.CustomerInformation
import com.anaraya.domain.entity.ProductStoreItemList

fun ProductStoreDto.toEntity(): List<ProductStoreItemList> {
    return data.itemsList.map {
        ProductStoreItemList(
            id = it.id,
            category = it.category,
            categoryId = it.categoryId,
            subCategory = it.subCategory,
            subCategoryId = it.subCategoryId,
            condition = it.condition,
            title = it.title,
            itemDescription = it.itemDescription,
            price = it.price,
            location = it.location,
            productImageUrl = it.productImageUrl,
            userAction = it.userAction,
            numberOfBuyers = it.numberOfBuyers,
            customerInformation = it.customerInformation.map { d ->
                CustomerInformation(
                    listeningId = d.listiningId,
                    id = d.id,
                    name = d.name,
                    nationalID = d.nationalID,
                    hrId = d.hrid,
                    phoneNumber = d.phoneNumber,
                    email = d.email,
                    payMethod = d.payMethod,
                    sellingStatus = d.sellingStatus
                )
            },
            customerWantsToBuy = it.customerWantsToBuy
        )
    }


}