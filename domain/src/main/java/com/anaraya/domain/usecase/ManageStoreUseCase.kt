package com.anaraya.domain.usecase

import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.TermsAndConditionException
import com.anaraya.domain.repo.IRepo
import java.io.File
import javax.inject.Inject

class ManageStoreUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getStoreProductCategories() = repo.getStoreProductCategory()
    suspend fun getStoreServicesCategories() = repo.getStoreServicesCategory()
    suspend fun getStoreSubCategory(categoryId: Int) = repo.getStoreSubCategory(categoryId)
    suspend fun getStoreProduct(statusId: Int) =
        repo.getStoreProduct(statusId)

    suspend fun getStoreProductByIdForOwner(productId: Int) =
        repo.getStoreProductByIdForOwner(productId)

    suspend fun getStoreProductByIdForCustomer(productId: Int) =
        repo.getStoreProductByIdForCustomer(productId)

    suspend fun getStoreServiceByIdForOwner(serviceId: Int) =
        repo.getStoreServiceByIdForOwner(serviceId)

    suspend fun getStoreServiceByIdForCustomer(serviceId: Int) =
        repo.getStoreServiceByIdForCustomer(serviceId)

    suspend fun proceedWithSale(listeningId: Int) = repo.proceedWithSale(listeningId)

    suspend fun confirmProductDeal(
        companyId: String?,
        governorateId: String?,
        listeningId: Int,
        companyAddressId: String?,
        paymentMethod: Int,
    ): BaseResponse {
        if (paymentMethod == 1 || paymentMethod == 2)
            checkCompanyAddressDataValidation(companyId, governorateId, companyAddressId)
        return repo.confirmProductDeal(
            listeningId,
            if (paymentMethod == 1 || paymentMethod == 2) companyAddressId else null,
            paymentMethod
        )
    }

    suspend fun confirmServiceDeal(
        companyId: String?,
        governorateId: String?,
        listeningId: Int,
        companyAddressId: String?,
        paymentMethod: Int,
    ): BaseResponse {
        if (paymentMethod == 1 || paymentMethod == 2)
            checkCompanyAddressDataValidation(companyId, governorateId, companyAddressId)
        return repo.confirmServiceDeal(
            listeningId,
            if (paymentMethod == 1 || paymentMethod == 2) companyAddressId else null,
            paymentMethod
        )
    }

    suspend fun getBankAccount() = repo.getBankAccount()


    suspend fun getStoreService(statusId: Int) =
        repo.getStoreService(statusId)

    suspend fun storeAddProduct(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        isAnonymous: Boolean?,
        handleDelivery: Boolean?,
        productImage: File?,
        accept: Boolean,
    ): BaseResponse {
        checkDataValidation(
            title,
            categoryId,
            subCategoryId,
            condition,
            itemDescription,
            price,
            location,
            isAnonymous,
            handleDelivery,
            productImage,
            accept
        )
        return repo.storeAddProduct(
            subCategoryId.toString(),
            condition.toString(),
            title!!,
            itemDescription!!,
            price!!,
            location!!,
            isAnonymous!!,
            handleDelivery!!,
            productImage!!,
        )
    }

    suspend fun storeUpdateProduct(
        id: Int,
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        isAnonymous: Boolean?,
        handleDelivery: Boolean?,
        productImage: File?,
        accept: Boolean,
        imageUrl: String?,
    ): BaseResponse {
        checkDataValidation(
            title,
            categoryId,
            subCategoryId,
            condition,
            itemDescription,
            price,
            location,
            isAnonymous,
            handleDelivery,
            productImage,
            accept,
            imageUrl
        )
        return repo.storeUpdateProduct(
            id = id,
            subCategoryId = subCategoryId.toString(),
            condition = condition.toString(),
            title = title!!,
            itemDescription = itemDescription!!,
            price = price!!,
            location = location!!,
            isAnonymous = isAnonymous!!,
            handleDelivery = handleDelivery!!,
            productImage = productImage,
        )
    }

    suspend fun storeAddService(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        serviceImage: File?,
        accept: Boolean,
        isServiceRental: Boolean?,
        fromDate: String,
        toDate: String,
    ): BaseResponse {
        checkDataValidationService(
            title,
            categoryId,
            subCategoryId,
            itemDescription,
            price,
            location,
            serviceImage,
            accept,
            isServiceRental
        )
        return repo.storeAddService(
            subCategoryId.toString(),
            title!!,
            itemDescription!!,
            price!!,
            location!!,
            isServiceRental!!,
            fromDate,
            toDate,
            serviceImage!!,
        )
    }

    suspend fun storeUpdateService(
        id: Int,
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        serviceImage: File?,
        accept: Boolean,
        isServiceRental: Boolean?,
        fromDate: String,
        toDate: String,
        imageUrl: String?,
    ): BaseResponse {
        checkDataValidationServiceUpdate(
            title,
            categoryId,
            subCategoryId,
            itemDescription,
            price,
            location,
            serviceImage,
            accept,
            imageUrl
        )
        return repo.storeUpdateService(
            id,
            subCategoryId.toString(),
            title!!,
            itemDescription!!,
            price!!,
            location!!,
            isServiceRental!!,
            fromDate,
            toDate,
            serviceImage,
        )
    }

    suspend fun getExploreProducts(
        searchWord: String? = null,
        searchLanguage: String? = null,
        catId: Int? = null,
        subCatId: Int? = null,
    ) = repo.getExploreProducts(searchWord, searchLanguage, catId, subCatId)

    suspend fun getExploreServices(
        searchWord: String? = null,
        searchLanguage: String? = null,
        catId: Int? = null,
        subCatId: Int? = null,
    ) = repo.getExploreServices(searchWord, searchLanguage, catId, subCatId)

    private fun checkDataValidation(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        isAnonymous: Boolean?,
        handleDelivery: Boolean?,
        productImage: File?,
        accept: Boolean,
        imageUrl: String? = null,
    ) {
        if (title.isNullOrEmpty())
            throw EmptyDataException("Title Field is Empty")
        if (categoryId == -1)
            throw EmptyDataException("Category Field is Empty")
        if (subCategoryId == -1)
            throw EmptyDataException("Sub Category Field is Empty")
        if (condition == -1)
            throw EmptyDataException("Condition Field is Empty")
        if (itemDescription.isNullOrEmpty())
            throw EmptyDataException("Description Field is Empty")
        if (price.isNullOrEmpty())
            throw EmptyDataException("Price Field is Empty")
        if (location.isNullOrEmpty())
            throw EmptyDataException("Location Field is Empty")
        if (isAnonymous == null)
            throw EmptyDataException("Anonymous Field is Empty")
        if (handleDelivery == null)
            throw EmptyDataException("Handle Delivery Field is Empty")
        if (productImage == null && imageUrl == null)
            throw EmptyDataException("Image Field is Empty")
        if (!accept)
            throw TermsAndConditionException("Must Accept Terms And Conditions")
    }

    private fun checkDataValidationService(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        productImage: File?,
        accept: Boolean,
        isServiceRental: Boolean?,
    ) {
        if (title.isNullOrEmpty())
            throw EmptyDataException("Title Field is Empty")
        if (categoryId == -1)
            throw EmptyDataException("Category Field is Empty")
        if (subCategoryId == -1)
            throw EmptyDataException("Sub Category Field is Empty")
        if (isServiceRental == null)
            throw EmptyDataException("Service Field is Empty")
        if (itemDescription.isNullOrEmpty())
            throw EmptyDataException("Description Field is Empty")
        if (price.isNullOrEmpty())
            throw EmptyDataException("Price Field is Empty")
        if (location.isNullOrEmpty())
            throw EmptyDataException("Location Field is Empty")
        if (productImage == null)
            throw EmptyDataException("Image Field is Empty")
        if (!accept)
            throw TermsAndConditionException("Must Accept Terms And Conditions")

    }

    private fun checkDataValidationServiceUpdate(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        productImage: File?,
        accept: Boolean,
        imageUrl: String?,
    ) {
        if (title.isNullOrEmpty())
            throw EmptyDataException("Title Field is Empty")
        if (categoryId == -1)
            throw EmptyDataException("Category Field is Empty")
        if (subCategoryId == -1)
            throw EmptyDataException("Sub Category Field is Empty")
        if (itemDescription.isNullOrEmpty())
            throw EmptyDataException("Description Field is Empty")
        if (price.isNullOrEmpty())
            throw EmptyDataException("Price Field is Empty")
        if (location.isNullOrEmpty())
            throw EmptyDataException("Location Field is Empty")
        if (productImage == null && imageUrl == null)
            throw EmptyDataException("Image Field is Empty")
        if (!accept)
            throw TermsAndConditionException("Must Accept Terms And Conditions")
    }

    suspend fun proceedWithRent(listeningId: Int) = repo.proceedWithRent(listeningId)

    suspend fun requestToDeleteProduct(customerProductId: Int) =
        repo.requestToDeleteProduct(customerProductId)

    suspend fun requestToDeleteService(customerProductId: Int) =
        repo.requestToDeleteService(customerProductId)

    suspend fun requestToBuy(productId: Int) = repo.requestToBuy(productId)
    suspend fun requestToRent(serviceId: Int, rentTo: String?, rentFrom: String?) =
        repo.requestToRent(serviceId = serviceId, rentTo = rentTo, rentFrom = rentFrom)

    private fun checkCompanyAddressDataValidation(
        company: String?,
        governorate: String?,
        companyAddress: String?,
    ) {
        if (company.isNullOrEmpty())
            throw EmptyDataException("Company Field is Empty")
        if (governorate.isNullOrEmpty())
            throw EmptyDataException("Governorate Company Field is Empty")
        if (companyAddress.isNullOrEmpty())
            throw EmptyDataException("Company Address Field is Empty")
    }
}