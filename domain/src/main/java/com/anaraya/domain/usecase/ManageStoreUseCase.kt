package com.anaraya.domain.usecase

import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.TermsAndConditionException
import com.anaraya.domain.repo.IRepo
import java.io.File
import javax.inject.Inject

class ManageStoreUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getStoreCategories(isProduct: Boolean) = repo.getStoreCategory(isProduct)
    suspend fun getStoreSubCategory(categoryId: Int) = repo.getStoreSubCategory(categoryId)
    suspend fun getStoreProductAndService(statusId: Int,isProduct: Boolean) = repo.getStoreProductAndService(statusId,isProduct)

    suspend fun storeAddProduct(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        productImage: File?,
        accept: Boolean,
        isProduct: Boolean
    ): BaseResponse {
        checkDataValidation(
            title,
            categoryId,
            subCategoryId,
            condition,
            itemDescription,
            price,
            location,
            productImage,
            accept
        )
        return repo.storeAdd(
            subCategoryId.toString(),
            condition.toString(),
            title!!,
            itemDescription!!,
            price!!,
            location!!,
            productImage!!,
            isProduct
        )
    }

    private fun checkDataValidation(
        title: String?,
        categoryId: Int,
        subCategoryId: Int,
        condition: Int,
        itemDescription: String?,
        price: String?,
        location: String?,
        productImage: File?,
        accept: Boolean
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
        if (productImage == null)
            throw EmptyDataException("Image Field is Empty")
        if (!accept)
            throw TermsAndConditionException("Must Accept Terms And Conditions")


    }

    suspend fun requestToDelete(customerProductId: Int,isProduct: Boolean) =
        repo.requestToDelete(customerProductId,isProduct)
}