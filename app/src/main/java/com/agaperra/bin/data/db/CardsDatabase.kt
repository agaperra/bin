package com.agaperra.bin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agaperra.bin.data.db.dao.CardsDao
import com.agaperra.bin.data.db.entity.CardItemEntity

@Database(
    entities = [CardItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CardsDatabase : RoomDatabase() {
    abstract fun cardsDao(): CardsDao
}