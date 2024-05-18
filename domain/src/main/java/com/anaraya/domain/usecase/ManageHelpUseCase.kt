package com.anaraya.domain.usecase

import com.anaraya.domain.entity.HelpDetails
import com.anaraya.domain.repo.IRepo
import javax.inject.Inject

class ManageHelpUseCase @Inject constructor(private val repo: IRepo) {
    suspend fun getAllHelp() = repo.getAllHelp()
    suspend fun getHelpDetails(helpId: Int) = repo.getHelpDetails(helpId)
}