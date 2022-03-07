package com.amd.efishery.assignment.di

import android.content.Context
import com.amd.efishery.assignment.data.EfisheryRepositoryImpl
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.LocalDataSourceImpl
import com.amd.efishery.assignment.data.local.LocalDb
import com.amd.efishery.assignment.data.remote.ApiService
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.data.remote.RemoteDataSourceImpl
import com.amd.efishery.assignment.utils.NetworkAwareHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    internal fun provideLocalDb(@ApplicationContext context: Context) = LocalDb(context)

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(
        apiService: ApiService,
        dispatcherThread: DispatcherThread
    ): RemoteDataSourceImpl {
        return RemoteDataSourceImpl(apiService, dispatcherThread)
    }

    @Singleton
    @Provides
    internal fun provideLocalDataSource(
        localDb: LocalDb
    ): LocalDataSourceImpl {
        return LocalDataSourceImpl(localDb)
    }

    @Singleton
    @Provides
    internal fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        networkAwareHandler: NetworkAwareHandler,
        dispatcherThread: DispatcherThread
    ): EfisheryRepositoryImpl {
        return EfisheryRepositoryImpl(
            localDataSource,
            remoteDataSource,
            networkAwareHandler,
            dispatcherThread
        )
    }

}