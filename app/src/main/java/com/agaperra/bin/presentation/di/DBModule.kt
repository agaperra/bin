package com.agaperra.bin.presentation.di

import android.content.Context
import androidx.room.Room
import com.agaperra.bin.data.db.CardsDatabase
import com.agaperra.bin.data.db.dao.CardsDao
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
    fun provideDatabase(@ApplicationContext context: Context): CardsDatabase = Room
        .databaseBuilder(context, CardsDatabase::class.java, "bin_database")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideCardsDao(cardsDatabase: CardsDatabase): CardsDao = cardsDatabase.cardsDao()


}