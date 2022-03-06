package com.amd.efishery.assignment.data

import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.domain.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {

    override suspend fun getProducts(limit: Int, offset: Int): List<ProductEntity> {
        return remoteDataSource.getProducts(limit, offset)
    }
}