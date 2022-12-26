package com.agaperra.bin.presentation.di

import android.content.Context
import androidx.room.Room
import com.agaperra.bin.data.db.BinDatabase
import com.agaperra.bin.data.db.dao.BinItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): BinDatabase = Room
        .databaseBuilder(context, BinDatabase::class.java, "bin_database")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideBinDao(binDatabase: BinDatabase): BinItemDao = binDatabase.itemDao()


}