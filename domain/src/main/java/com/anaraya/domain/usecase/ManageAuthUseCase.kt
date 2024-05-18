package com.anaraya.domain.usecase

import com.anaraya.domain.entity.Auth
import com.anaraya.domain.entity.ResetChangePass
import com.anaraya.domain.exception.EmailInValidFormatException
import com.anaraya.domain.exception.EmptyDataException
import com.anaraya.domain.exception.PasswordInValidFormatException
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageAuthUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun checkAuth(
        rayaId: String,
        nationalId: String,
    ) = repo.checkAuth(rayaId, nationalId)


    suspend fun signIn(
        rayaId: String?,
        nationalId: String?,
        password: String?,
    ): Auth {
        validateSignInData(rayaId, nationalId, password)
        return repo.signIn(rayaId!!.toInt(), nationalId!!.toLong(), password!!)
    }

    suspend fun forgetPass(
        rayaId: String?,
        nationalId: String?,
    ): ResetChangePass {
        //validateForgetPass(nationalId,rayaId)
        return repo.forgetPass(rayaId!!, nationalId!!)
    }

    suspend fun forgetPassCheckCode(
        rayaId: String?,
        nationalId: String?,
        code: String?,
    ): ResetChangePass {
        validateForgetPassCheckCode(rayaId, nationalId, code)
        return repo.forgetPassCheckCode(rayaId!!, nationalId!!, code!!)
    }

    suspend fun resetPass(
        rayaId: String,
        nationalId: String,
        code: String,
        newPass: String?,
    ): ResetChangePass {
        validateResetPass(newPass)
        return repo.resetPass(rayaId, nationalId, code, newPass!!)
    }

    suspend fun signUp(
        rayaId: String?,
        nationalId: String?,
        name: String?,
        email: String?,
        password: String?,
        phone: String?,
        dateOfBirth: String?,
        gender: Int?,
        addressLabel: String?,
        governorate: String?,
        district: String?,
        address: String?,
        street: String?,
        building: String?,
        landmark: String?,
    ): Auth {
        validateSignUpData(name, email, password, phone)
        return repo.signIup(
            rayaId!!,
            nationalId!!,
            name!!,
            email,
            password!!,
            phone!!,
            dateOfBirth,
            gender,
            addressLabel,
            governorate,
            district,
            address,
            street,
            building,
            landmark
        )
    }

    private fun validateSignInData(
        rayaId: String?,
        nationalId: String?,
        password: String?,
    ) {
        if (rayaId.isNullOrEmpty())
            throw EmptyDataException("Raya Id is empty")
        if (nationalId.isNullOrEmpty())
            throw EmptyDataException("National Id is empty")
        if (password.isNullOrEmpty())
            throw PasswordInValidFormatException("password must have at least 8 characters")
        validatePass(password)
    }

    fun validateSignUpId(
        rayaId: String?,
        nationalId: String?,
    ) {
        if (rayaId.isNullOrEmpty())
            throw EmptyDataException("Raya Id is empty")
        if (nationalId.isNullOrEmpty())
            throw EmptyDataException("National Id is empty")
    }

    private fun validateSignUpData(
        name: String?,
        email: String?,
        password: String?,
        phone: String?
    ) {
        if (name.isNullOrEmpty())
            throw EmptyDataException("Name is empty")
        if (!email.isNullOrEmpty()) {
            validateEmail(email)
        }
        if (phone.isNullOrEmpty())
            throw EmptyDataException("Mobile Phone is empty")
        if (password.isNullOrEmpty())
            throw PasswordInValidFormatException("password must have at least 8 characters")
        validatePass(password)

    }

    private fun validateForgetPassCheckCode(
        rayaId: String?,
        nationalId: String?,
        code: String?,
    ) {
        if (rayaId.isNullOrEmpty())
            throw EmptyDataException("Raya Id is empty")
        if (nationalId.isNullOrEmpty())
            throw EmptyDataException("National Id is empty")
        if (code.isNullOrEmpty())
            throw EmptyDataException("Verification Code is empty")
    }

    private fun validateResetPass(
        newPass: String?,
    ) {
        if (newPass.isNullOrEmpty())
            throw EmptyDataException("New Password is empty")
        validatePass(newPass)

    }

    private fun validatePass(password: String) {
        if (password.count() < 8)
            throw PasswordInValidFormatException("password must have at least 8 characters")
        else {
            if (password.none { it.isUpperCase() })
                throw PasswordInValidFormatException("password must have at least one uppercase")
            else {
                if (password.none { it.isLowerCase() })
                    throw PasswordInValidFormatException("password must have at least one lowercase")
                else {
                    if (password.none { it.isDigit() })
                        throw PasswordInValidFormatException("password must have at least one digit")
                    else {
                        if (!password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()))
                            throw PasswordInValidFormatException("password must have at least one special character")
                    }
                }
            }
        }
    }

    private fun validateEmail(email: String) {
        if (!email.contains("@"))
            throw EmailInValidFormatException("email invalid format")
        if (!email.contains("."))
            throw EmailInValidFormatException("email invalid format")
    }
}