package com.amd.efishery.assignment.di

import com.amd.efishery.assignment.data.EfisheryRepositoryImpl
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.LocalDataSourceImpl
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.data.remote.RemoteDataSourceImpl
import com.amd.efishery.assignment.domain.EfisheryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl) : LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(localDataSourceImpl: RemoteDataSourceImpl) : RemoteDataSource

    @Binds
    abstract fun provideProductRepository(localDataSourceImpl: EfisheryRepositoryImpl) : EfisheryRepository

    @Binds
    abstract fun provideDispatcher(dispatcher: DispatcherProvider): DispatcherThread

}