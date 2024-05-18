package com.anaraya.domain.usecase

import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.Referrals
import com.anaraya.domain.exception.EmailInValidFormatException
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageFamilyUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getAllRelationships() = repo.getAllRelationships()
    suspend fun getAllReferrals() = repo.getAllReferrals()

    suspend fun addNewReferral(
        name: String?,
        phoneNumber: String?,
        relationshipId: Int?,
        email: String? = null
    ): BaseResponse {
        validateRelationshipData(name, phoneNumber, relationshipId, email)
        return repo.addNewReferral(name!!, phoneNumber!!, relationshipId!!, email)
    }


    private fun validateRelationshipData(
        name: String?,
        phone: String?,
        relationship: Int?,
        email: String?,
    ) {
        if (name.isNullOrEmpty())
            throw EmptyDataException("Name is empty")
        if (phone.isNullOrEmpty())
            throw EmptyDataException("Mobile Phone is empty")
        if (relationship == null)
            throw EmptyDataException("Relationship is empty")
        if (!email.isNullOrEmpty()) {
            validateEmail(email)
        }
    }

    private fun validateEmail(email: String) {
        if (!email.contains("@"))
            throw EmailInValidFormatException("email invalid format")
        if (!email.contains("."))
            throw EmailInValidFormatException("email invalid format")

    }
}