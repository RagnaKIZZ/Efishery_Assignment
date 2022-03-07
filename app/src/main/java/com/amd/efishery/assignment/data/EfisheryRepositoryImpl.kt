package com.amd.efishery.assignment.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.domain.EfisheryRepository
import com.amd.efishery.assignment.domain.paging.ProductMediator
import javax.inject.Inject

class EfisheryRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : EfisheryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getProducts(): Pager<Int, ProductEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = true),
            remoteMediator = ProductMediator(localDataSource, remoteDataSource)
        ) {
            localDataSource.getProductPaging()
        }
    }
}