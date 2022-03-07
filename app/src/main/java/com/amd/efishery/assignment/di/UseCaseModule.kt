package com.amd.efishery.assignment.di

import com.amd.efishery.assignment.domain.EfisheryRepository
import com.amd.efishery.assignment.domain.usecase.ProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    internal fun provideProductUseCase(repository: EfisheryRepository) = ProductUseCase(repository)

}