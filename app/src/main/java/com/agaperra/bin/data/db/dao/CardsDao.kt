package com.agaperra.bin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agaperra.bin.data.db.entity.CardItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CardItemEntity)

    @Query("SELECT * FROM items_table")
    fun getItems(): Flow<List<CardItemEntity>>

}