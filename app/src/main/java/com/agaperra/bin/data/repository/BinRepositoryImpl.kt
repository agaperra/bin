package com.agaperra.bin.data.repository

import com.agaperra.bin.data.api.BinApi
import com.agaperra.bin.data.db.dao.BinItemDao
import com.agaperra.bin.data.db.entity.BinItemEntity
import com.agaperra.bin.data.db.toDomain
import com.agaperra.bin.data.db.toEntity
import com.agaperra.bin.data.remote.dto.BinResponse
import com.agaperra.bin.domain.model.BinItem
import com.agaperra.bin.domain.repository.BinRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class BinRepositoryImpl @Inject constructor(
    private val binApi: BinApi,
    private val listDao: BinItemDao
) : BinRepository {

    override suspend fun mainContent(
        number: String?
    ): BinResponse =
        binApi.mainContent(
            number = number
        )

    override  fun historyContent(): Flow<List<BinItemEntity>> = listDao.getItems()

    override suspend fun insertBin(item: BinItem) {
        listDao.insert(item.toEntity())
    }
}
