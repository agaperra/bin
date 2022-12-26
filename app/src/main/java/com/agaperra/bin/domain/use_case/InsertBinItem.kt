package com.agaperra.bin.domain.use_case

import com.agaperra.bin.domain.model.BinItem
import com.agaperra.bin.domain.repository.BinRepository
import javax.inject.Inject

class InsertBinItem @Inject constructor(private val binRepository: BinRepository) {

    suspend operator fun invoke(bin: BinItem) =
        binRepository.insertBin(bin)

}