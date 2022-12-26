package com.agaperra.bin.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class BinItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "number")
    val number: String
)