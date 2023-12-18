package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.model.CardItem
import com.agaperra.bin.domain.repository.CardsRepository
import javax.inject.Inject

class InsertBinItem @Inject constructor(private val binRepository: CardsRepository) {

    suspend operator fun invoke(bin: CardItem) =
        binRepository.insertBin(bin)

}