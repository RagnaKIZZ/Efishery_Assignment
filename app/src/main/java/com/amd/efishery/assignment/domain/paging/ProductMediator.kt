package com.amd.efishery.assignment.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.RemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class ProductMediator(
    private val limit: Int,
    private val offset: Int,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RemoteMediator<Int, ProductEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}