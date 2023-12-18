package com.agaperra.bin.data.repository

import com.agaperra.bin.data.api.BinApi
import com.agaperra.bin.data.db.ConverterEntityToDomain.toEntity
import com.agaperra.bin.data.db.dao.CardsDao
import com.agaperra.bin.data.db.entity.CardItemEntity
import com.agaperra.bin.data.remote.dto.CarsAnswerResponse
import com.agaperra.bin.domain.model.CardItem
import com.agaperra.bin.domain.repository.CardsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class CardsRepositoryImpl @Inject constructor(
    private val binApi: BinApi,
    private val listDao: CardsDao
) : CardsRepository {

    override suspend fun getInfoByCardNumber(
        number: String?
    ): CarsAnswerResponse =
        binApi.getCardInfoByNumber(
            number = number
        )

    override  fun historyContent(): Flow<List<CardItemEntity>> = listDao.getItems()

    override suspend fun insertBin(item: CardItem) {
        listDao.insert(item.toEntity())
    }
}
