package com.agaperra.bin.domain.repository

import com.agaperra.bin.data.db.entity.BinItemEntity
import com.agaperra.bin.data.remote.dto.BinResponse
import com.agaperra.bin.domain.model.BinItem
import kotlinx.coroutines.flow.Flow

interface BinRepository {

    suspend fun mainContent(
        number: String?
    ): BinResponse

    fun historyContent(
    ): Flow<List<BinItemEntity>>

    suspend fun insertBin(item: BinItem)

}