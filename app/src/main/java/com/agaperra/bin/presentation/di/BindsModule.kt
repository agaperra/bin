package com.agaperra.bin.presentation.di

import com.agaperra.bin.data.repository.CardsRepositoryImpl
import com.agaperra.bin.domain.interactor.StringInteractor
import com.agaperra.bin.domain.repository.CardsRepository
import com.agaperra.bin.presentation.interactor.StringInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface BindsModule {
    @Binds
    fun bindRepository(repositoryImpl: CardsRepositoryImpl): CardsRepository


    @Binds
    fun bindStringInteractor(stringInteractorImpl: StringInteractorImpl): StringInteractor

}