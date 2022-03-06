package com.amd.efishery.assignment.data.local

import com.amd.efishery.assignment.data.local.entity.LocalDb
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.di.DispatcherThread
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface LocalDataSource {
    suspend fun upsertProduct(list: List<ProductEntity>)
}

class LocalDataSourceImpl @Inject constructor(
    private val database: LocalDb,
    private val dispatcher: DispatcherThread
) : LocalDataSource {

    override suspend fun upsertProduct(list: List<ProductEntity>) {
        flowOf(database.productDao().upsertProduct(list)).flowOn(dispatcher.io)
            .catch { e ->
                e.printStackTrace()
            }
    }
}