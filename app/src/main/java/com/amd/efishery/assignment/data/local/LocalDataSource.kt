package com.amd.efishery.assignment.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.local.entity.RemoteKey
import com.amd.efishery.assignment.di.DispatcherThread
import com.amd.efishery.assignment.utils.Constants
import javax.inject.Inject

interface LocalDataSource {
    //get product
    suspend fun insertDataToCache(list: List<ProductEntity>, offset: Int, isRefresh: Boolean)
    suspend fun getRemoteKeyById(uuid: String): RemoteKey?
    fun getProductPaging(): PagingSource<Int, ProductEntity>
    suspend fun deleteProductItem(uuid: String)

    //size
    suspend fun insertSize(list: List<OptionSizeEntity>)
    suspend fun getSizes(): List<OptionSizeEntity>

    //area
    suspend fun insertArea(list: List<OptionAreaEntity>)
    suspend fun getArea(): List<OptionAreaEntity>
    suspend fun getAreaByProvince(province: String) : List<OptionAreaEntity>
}

class LocalDataSourceImpl @Inject constructor(
    private val database: LocalDb
) : LocalDataSource {

    override suspend fun getRemoteKeyById(uuid: String): RemoteKey? {
        return database.remoteKeyDao().remoteKeysProductId(uuid)
    }

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

    //size
    override suspend fun insertSize(list: List<OptionSizeEntity>) {
        database.withTransaction {
            database.sizeDao().deleteSize()
            database.sizeDao().insertSize(list)
        }
    }

    override suspend fun getSizes(): List<OptionSizeEntity> {
        return database.sizeDao().getAllSize()
    }

    //area
    override suspend fun insertArea(list: List<OptionAreaEntity>) {
        database.withTransaction {
            database.areaDao().deleteArea()
            database.areaDao().insertArea(list)
        }
    }

    override suspend fun getArea(): List<OptionAreaEntity> {
        return database.areaDao().getAllArea()
    }

    override suspend fun getAreaByProvince(province: String): List<OptionAreaEntity> {
        return database.areaDao().getAllAreaByProvince(province)
    }

    override suspend fun deleteProductItem(uuid: String) {
        database.productDao().deleteProduct(uuid)
    }
}