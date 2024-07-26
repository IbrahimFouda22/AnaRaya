package com.anaraya.data.mapper

import com.anaraya.data.dto.ServiceStoreDto
import com.anaraya.data.dto.ServiceStoreDtoDetailsData
import com.anaraya.data.dto.ServiceStoreDtoDetailsForCustomer
import com.anaraya.data.dto.ServiceStoreDtoItemList
import com.anaraya.domain.entity.ServiceCustomerInformation
import com.anaraya.domain.entity.ServiceStoreForCustomer
import com.anaraya.domain.entity.ServiceStoreItemList

fun ServiceStoreDto.toEntity(): List<ServiceStoreItemList> {
    return data.itemsList.map { item ->
        item.toEntity()
    }
}

fun ServiceStoreDtoDetailsData.toEntity(): ServiceStoreItemList {
    return data.toEntity()
}

fun ServiceStoreDtoItemList.toEntity(): ServiceStoreItemList {
    return ServiceStoreItemList(
        id = id,
        category = category,
        categoryId = categoryId,
        subCategory = subCategory,
        subCategoryId = subCategoryId,
        title = title,
        serviceDescription = serviceDescription,
        price = price,
        location = location,
        serviceImageUrl = serviceImageUrl,
        userAction = userAction,
        numberOfBuyers = numberOfBuyers,
        status = status,
        itIsARent = itIsARent,
        rentFrom = rentFrom ?: "",
        rentTo = rentTo ?: "",
        customerInformation = customerInformation.map { d ->
            ServiceCustomerInformation(
                listiningId = d.listiningId,
                id = d.id,
                name = d.name,
                nationalID = d.nationalID,
                hrid = d.hrid,
                phoneNumber = d.phoneNumber,
                email = d.email ?: "",
                payMethod = d.payMethod ?: -1,
                rentStatus = d.rentStatus ?: 0,
                rentFrom = d.rentFrom ?: "",
                rentTo = d.rentTo ?: ""
            )
        },
        customerWantsToRent = customerWantsToRent
    )
}

fun ServiceStoreDtoDetailsForCustomer.toEntity(): ServiceStoreForCustomer {
    return ServiceStoreForCustomer(
        id = data.id,
        category = data.category,
        subCategory = data.subCategory,
        title = data.title,
        serviceDescription = data.serviceDescription,
        price = data.price,
        location = data.location,
        serviceImageUrl = data.serviceImageUrl,
        sellerName = data.sellerName ?: "",
        rentStatus = data.rentStatus ?: 0,
        sellerPhoneNumber = data.sellerPhoneNumber ?: "",
        isUserService = data.isUserService,
        itIsARent = data.itIsARent,
        baseRentFrom = data.baseRentFrom ?: "",
        baseRentTo = data.baseRentTo ?: "",
        rentFrom = data.rentFrom ?: "",
        rentTo = data.rentTo ?: ""
    )
}