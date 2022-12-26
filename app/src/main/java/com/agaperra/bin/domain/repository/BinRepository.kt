package com.agaperra.bin.domain.repository

import com.agaperra.bin.domain.model.BinResponse

interface BinRepository {

    suspend fun mainContent(
        number: String?
    ): BinResponse

}