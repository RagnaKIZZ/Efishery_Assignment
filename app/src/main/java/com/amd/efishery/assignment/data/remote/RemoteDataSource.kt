package com.amd.efishery.assignment.data.remote

import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.DeleteProductParams
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.product.SuccessCreate
import com.amd.efishery.assignment.data.remote.model.size.OptionSize
import com.amd.efishery.assignment.di.DispatcherThread
import com.amd.efishery.assignment.domain.mapper.toEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getProducts(limit: Int = 10, offset: Int = 0): List<ProductEntity>
    suspend fun insertProduct(body: ProductItem): Boolean
    suspend fun deleteProduct(body: DeleteProductParams): Boolean
    suspend fun getSizes(): List<OptionSizeEntity>
    suspend fun getAreas(): List<OptionAreaEntity>
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: DispatcherThread
) : RemoteDataSource {

    override suspend fun getProducts(limit: Int, offset: Int): List<ProductEntity> {
        val products = mutableListOf<ProductEntity>()
        try {
            flowOf(apiService.getProducts(limit, offset))
                .flowOn(dispatcher.io)
                .catch { e ->
                    e.printStackTrace()
                }.map {
                    it.map { item -> item.toEntity() }
                }.collectLatest {
                    products.addAll(it)
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return products
    }

    override suspend fun insertProduct(body: ProductItem): Boolean {
        var status = false
        try {
            flowOf(apiService.createProduct(listOf(body)))
                .flowOn(dispatcher.io)
                .catch { e ->
                    e.printStackTrace()
                }.collectLatest {
                    status = true
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return status
    }

    override suspend fun deleteProduct(body: DeleteProductParams): Boolean {
        var status = false
        try {
            flowOf(apiService.deleteProduct(body)).catch { e ->
                e.printStackTrace()
            }.flowOn(dispatcher.io)
                .catch { e ->
                    e.printStackTrace()
                }.collectLatest {
                    status = it.clearedRowsCount ?: 0 > 0
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return status
    }

    override suspend fun getSizes(): List<OptionSizeEntity> {
        val optionSize = mutableListOf<OptionSizeEntity>()
        try {
            flowOf(apiService.getSizes(35, 0))
                .flowOn(dispatcher.io)
                .catch { e ->
                    e.printStackTrace()
                }.map {
                    it.map { item -> item.toEntity() }
                }.collectLatest {
                    optionSize.addAll(it)
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return optionSize
    }

    override suspend fun getAreas(): List<OptionAreaEntity> {
        val optionArea = mutableListOf<OptionAreaEntity>()
        try {
            flowOf(apiService.getAreas(35, 0))
                .flowOn(dispatcher.io)
                .catch { e ->
                    e.printStackTrace()
                }.map {
                    it.map { item -> item.toEntity() }
                }.collectLatest {
                    optionArea.addAll(it)
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return optionArea
    }
}