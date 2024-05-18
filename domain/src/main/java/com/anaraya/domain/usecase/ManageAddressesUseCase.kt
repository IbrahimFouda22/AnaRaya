package com.anaraya.domain.usecase

import com.anaraya.domain.entity.AddUpdateAddress
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageAddressesUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getAddresses() = repo.getAddress()
    suspend fun getAllCompanies() = repo.getAllCompanies()
    suspend fun getAllGovernorateByCompanyId(companyId: Int) =
        repo.getAllGovernorateByCompanyId(companyId)

    suspend fun getAllCompanyAddresses(companyId: Int, governorate: String) =
        repo.getAllCompanyAddresses(companyId, governorate)

    suspend fun addCompanyAddress(
        company: String?,
        governorate: String?,
        companyAddress: String?,
    ): AddUpdateAddress {
        checkCompanyAddressDataValidation(
            company,
            governorate,
            companyAddress,
        )
        return repo.addCompanyAddress(
            companyAddress!!
        )
    }

    suspend fun changeAddressDefault(addressId: String, userOrCompanyAddress: Boolean) =
        repo.changeDefaultAddress(addressId, userOrCompanyAddress)

    suspend fun addAddress(
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ): AddUpdateAddress {
        checkDataValidation(
            addressLabel,
            governorate,
            district,
            address,
            street,
            building,
            landmark,
        )
        return repo.addAddress(
            addressLabel!!, governorate!!,
            district!!, address!!, street!!, building!!, landmark!!
        )
    }

    suspend fun updateAddress(
        id: String,
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ): AddUpdateAddress {
        checkDataValidation(
            addressLabel,
            governorate,
            district,
            address,
            street,
            building,
            landmark,
        )
        return repo.updateAddress(
            id,
            addressLabel!!, governorate!!,
            district!!, address!!, street!!, building!!, landmark!!
        )
    }

    suspend fun deleteAddress(addressId: String) = repo.deleteAddress(addressId)


    fun checkDataValidation(
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ) {
        if (addressLabel.isNullOrEmpty())
            throw EmptyDataException("Address Label Field is Empty")
        if (governorate.isNullOrEmpty())
            throw EmptyDataException("Governorate Field is Empty")
        if (district.isNullOrEmpty())
            throw EmptyDataException("District Field is Empty")
        if (address.isNullOrEmpty())
            throw EmptyDataException("Address Field is Empty")
        if (street.isNullOrEmpty())
            throw EmptyDataException("Street Field is Empty")
        if (building.isNullOrEmpty())
            throw EmptyDataException("Building Field is Empty")
        if (landmark.isNullOrEmpty())
            throw EmptyDataException("Landmark Field is Empty")

    }

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