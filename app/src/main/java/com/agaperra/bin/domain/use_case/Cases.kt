package com.agaperra.bin.domain.use_case

import javax.inject.Inject

data class Cases @Inject constructor(
    val insertBinItem: InsertBinItem,
    val getCardBinInfo: GetCardBinInfo,
    val getBinHistoryList: GetBinHistoryList
)