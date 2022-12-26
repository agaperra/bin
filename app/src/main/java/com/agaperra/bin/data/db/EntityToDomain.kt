package com.agaperra.bin.data.db

import com.agaperra.bin.data.db.entity.BinItemEntity
import com.agaperra.bin.domain.model.BinItem

fun BinItemEntity.toDomain(): BinItem =
    BinItem(number = number)

fun BinItem.toEntity(): BinItemEntity =
    BinItemEntity(number = number!!)