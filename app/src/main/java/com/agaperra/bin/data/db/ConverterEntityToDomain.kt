package com.agaperra.bin.data.db

import com.agaperra.bin.data.db.entity.CardItemEntity
import com.agaperra.bin.domain.model.CardItem

object ConverterEntityToDomain {
    fun CardItemEntity.toDomain(): CardItem =
        CardItem(number = number)

    fun CardItem.toEntity(): CardItemEntity =
        CardItemEntity(number = number!!)
}