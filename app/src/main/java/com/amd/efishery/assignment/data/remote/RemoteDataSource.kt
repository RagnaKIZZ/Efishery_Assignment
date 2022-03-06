package com.amd.efishery.assignment.data.remote

import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.di.DispatcherThread
import com.amd.efishery.assignment.domain.mapper.toEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getProducts(limit: Int = 10, offset: Int = 0): List<ProductEntity>
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: DispatcherThread
) : RemoteDataSource {

    override suspend fun getProducts(limit: Int, offset: Int): List<ProductEntity> {
        val products = mutableListOf<ProductEntity>()
        flowOf(apiService.getProducts(limit, offset))
            .flowOn(dispatcher.io)
            .catch { e ->
                e.printStackTrace()
            }.map {
                it.map { item -> item.toEntity() }
            }.collectLatest {
                products.addAll(it)
            }
        return products
    }
}