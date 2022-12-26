package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.repository.BinRepository
import javax.inject.Inject

class GetBinHistoryList @Inject constructor(private val binRepository: BinRepository) {

    operator fun invoke() = binRepository.historyContent()

}