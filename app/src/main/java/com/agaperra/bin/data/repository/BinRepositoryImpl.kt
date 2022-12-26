package com.agaperra.bin.data.repository

import com.agaperra.bin.data.api.BinApi
import com.agaperra.bin.domain.model.BinResponse
import com.agaperra.bin.domain.repository.BinRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class BinRepositoryImpl @Inject constructor(
    private val binApi: BinApi
) : BinRepository {

    override suspend fun mainContent(
        number: String?
    ): BinResponse =
        binApi.mainContent(
            number = number
        )
}
