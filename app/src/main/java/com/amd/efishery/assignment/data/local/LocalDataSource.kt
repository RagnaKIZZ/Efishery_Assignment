package com.amd.efishery.assignment.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.local.entity.RemoteKey
import com.amd.efishery.assignment.di.DispatcherThread
import com.amd.efishery.assignment.utils.Constants
import javax.inject.Inject

interface LocalDataSource {
    suspend fun insertDataToCache(list: List<ProductEntity>, offset: Int, isRefresh: Boolean)
    fun getProductPaging(): PagingSource<Int, ProductEntity>
    suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProductEntity>): RemoteKey?
    suspend fun getLastRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey?
    suspend fun getFirstRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey?
}

class LocalDataSourceImpl @Inject constructor(
    private val database: LocalDb,
    private val dispatcher: DispatcherThread
) : LocalDataSource {

    override suspend fun insertDataToCache(
        list: List<ProductEntity>,
        offset: Int,
        isRefresh: Boolean
    ) {
        database.withTransaction {
            if (isRefresh) {
                database.productDao().deleteProduct()
                database.remoteKeyDao().deleteAll()
            }
            val prevKey = if (offset == Constants.INITIAL_OFFSET_PAGE) null else offset - 10
            val nextKey = if (list.isEmpty()) null else offset + 10
            val keys = list.map {
                RemoteKey(it.uuid, prevKey = prevKey, nextKey = nextKey)
            }
            database.remoteKeyDao().insertAll(keys)
            database.productDao().upsertProduct(list)
        }
    }

    override fun getProductPaging(): PagingSource<Int, ProductEntity> {
        return database.productDao().getProductListPaged()
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uuid?.let { repoId ->
                database.remoteKeyDao().remoteKeysProductId(repoId)
            }
        }
    }

    override suspend fun getLastRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { product -> database.remoteKeyDao().remoteKeysProductId(product.uuid) }
    }

    override suspend fun getFirstRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { product -> database.remoteKeyDao().remoteKeysProductId(product.uuid) }
    }
}