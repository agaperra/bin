package com.agaperra.bin.domain.repository

import com.agaperra.bin.data.db.entity.CardItemEntity
import com.agaperra.bin.data.remote.dto.CarsAnswerResponse
import com.agaperra.bin.domain.model.CardItem
import kotlinx.coroutines.flow.Flow

interface CardsRepository {

    suspend fun getInfoByCardNumber(
        number: String?
    ): CarsAnswerResponse

    fun historyContent(
    ): Flow<List<CardItemEntity>>

    suspend fun insertBin(item: CardItem)

}