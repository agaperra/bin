package com.agaperra.bin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agaperra.bin.data.db.dao.BinItemDao
import com.agaperra.bin.data.db.entity.BinItemEntity

@Database(
    entities = [BinItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BinDatabase : RoomDatabase() {
    abstract fun itemDao(): BinItemDao
}