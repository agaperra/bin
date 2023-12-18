package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.repository.CardsRepository
import javax.inject.Inject

class GetBinHistoryList @Inject constructor(private val binRepository: CardsRepository) {

    operator fun invoke() = binRepository.historyContent()

}