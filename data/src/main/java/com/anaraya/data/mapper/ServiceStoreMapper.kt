package com.anaraya.data.mapper

import com.anaraya.data.dto.ServiceStoreDto
import com.anaraya.domain.entity.ServiceCustomerInformation
import com.anaraya.domain.entity.ServiceStoreItemList

fun ServiceStoreDto.toEntity(): List<ServiceStoreItemList> {
    return data.itemsList.map {
        ServiceStoreItemList(
            id = it.id,
            category = it.category,
            categoryId = it.categoryId,
            subCategory = it.subCategory,
            subCategoryId = it.subCategoryId,
            title = it.title,
            serviceDescription = it.serviceDescription,
            price = it.price,
            location = it.location,
            serviceImageUrl = it.serviceImageUrl,
            userAction = it.userAction,
            numberOfBuyers = it.numberOfBuyers,
            customerInformation = it.customerInformation.map { d ->
                ServiceCustomerInformation(
                    listiningId = d.listiningId,
                    id = d.id,
                    name = d.name,
                    nationalID = d.nationalID,
                    hrid = d.hrid,
                    phoneNumber = d.phoneNumber,
                    email = d.email,
                    payMethod = d.payMethod,
                    rentStatus = d.rentStatus,
                    rentFrom = d.rentFrom,
                    rentTo = d.rentTo
                )
            },
            customerWantsToRent = it.customerWantsToRent
        )
    }


}