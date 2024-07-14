package com.anaraya.domain.usecase

import com.anaraya.domain.entity.EditInfo
import com.anaraya.domain.exception.CurrentPasswordInValidFormatException
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.NewPasswordInValidFormatException
import com.anaraya.domain.repo.IRepo
import java.io.File
import javax.inject.Inject

class ManageProfileUseCase @Inject constructor(private val repo: IRepo) {

    suspend fun getProfileData() = repo.getProfileData()
    suspend fun sendFCMToken(token: String, enabled: Boolean) = repo.sendFCMToken(token, enabled)
    suspend fun updateFCMToken(token: String, enabled: Boolean) = repo.updateFCMToken(token, enabled)

    suspend fun getUserLoyaltyPoints() = repo.getUserLoyaltyPoints()
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

    suspend fun updateDOB(dob: String?): EditInfo {
        validateData(dob)
        return repo.updateDOB(dob!!)
    }

    suspend fun updateGender(gender: Int): EditInfo {
        return repo.updateGender(gender)
    }

    private fun validateData(data: String?) {
        if (data.isNullOrEmpty())
            throw EmptyDataException("Empty Field required")
    }

    suspend fun verifyPhone(
        code: String,
    ) = repo.verifyPhone(code)

    suspend fun sendPhoneOtp() = repo.sendPhoneOtp()

    suspend fun changePassword(currentPassword: String, newPassword: String): EditInfo {
        validatePassword(currentPassword, newPassword)
        return repo.changePassword(currentPassword, newPassword)
    }

    private fun validatePassword(
        currentPassword: String?,
        newPass: String?,
    ) {
        if (currentPassword.isNullOrEmpty())
            throw CurrentPasswordInValidFormatException("password must have at least 8 characters")
        if (newPass.isNullOrEmpty())
            throw NewPasswordInValidFormatException("password must have at least 8 characters")
        validatePass(currentPassword, newPass)
    }

    private fun validatePass(currentPassword: String, newPassword: String) {
        if (currentPassword.count() < 8)
            throw CurrentPasswordInValidFormatException("password must have at least 8 characters")
        else {
            if (currentPassword.none { it.isUpperCase() })
                throw CurrentPasswordInValidFormatException("password must have at least one uppercase")
            else {
                if (currentPassword.none { it.isLowerCase() })
                    throw CurrentPasswordInValidFormatException("password must have at least one lowercase")
                else {
                    if (currentPassword.none { it.isDigit() })
                        throw CurrentPasswordInValidFormatException("password must have at least one digit")
                    else {
                        if (!currentPassword.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()))
                            throw CurrentPasswordInValidFormatException("password must have at least one special character")
                    }
                }
            }
        }
        if (newPassword.count() < 8)
            throw NewPasswordInValidFormatException("password must have at least 8 characters")
        else {
            if (newPassword.none { it.isUpperCase() })
                throw NewPasswordInValidFormatException("password must have at least one uppercase")
            else {
                if (newPassword.none { it.isLowerCase() })
                    throw NewPasswordInValidFormatException("password must have at least one lowercase")
                else {
                    if (newPassword.none { it.isDigit() })
                        throw NewPasswordInValidFormatException("password must have at least one digit")
                    else {
                        if (!newPassword.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()))
                            throw NewPasswordInValidFormatException("password must have at least one special character")
                    }
                }
            }
        }
    }

}