package com.anaraya.domain.usecase

import com.anaraya.domain.entity.BaseResponse
import com.anaraya.domain.entity.EditInfo
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.repo.IRepo
import java.io.File
import javax.inject.Inject

class ManageProfileUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getProfileData() = repo.getProfileData()
    suspend fun uploadImage(file: File) = repo.uploadImage(file)
    suspend fun updateName(name: String?): EditInfo {
        validateData(name)
        return repo.updateName(name!!)
    }

    suspend fun updateEmail(email: String?): EditInfo {
        validateData(email)
        return repo.updateEmail(email!!)
    }

    suspend fun updatePhoneNumber(phone: String?): EditInfo {
        validateData(phone)
        return repo.updatePhoneNumber(phone!!)
    }

    private fun validateData(data: String?) {
        if (data.isNullOrEmpty())
            throw EmptyDataException("Empty Field required")
    }

    suspend fun verifyPhone(
        code: String,
    ) = repo.verifyPhone(code)

    suspend fun sendPhoneOtp() = repo.sendPhoneOtp()

}