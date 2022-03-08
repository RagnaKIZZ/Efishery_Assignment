package com.amd.efishery.assignment.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.local.entity.RemoteKey
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
import com.amd.efishery.assignment.domain.mapper.toEntity
import com.amd.efishery.assignment.utils.Constants
import com.amd.efishery.assignment.utils.logging
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductMediator(
    private val searchProductParam: SearchProductParam? = null,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            logging(searchProductParam.toString())
            logging(loadType.name)
            val offset = when (val pageKeyData = getKeyLimitData(loadType, state)) {
                is MediatorResult.Success -> {
                    return pageKeyData
                }
                else -> {
                    pageKeyData as Int
                }
            }

            val response = remoteDataSource.getProducts(
                Constants.DEFAULT_SIZE_PAGE,
                offset,
                searchProductParam
            )

            localDataSource.insertDataToCache(
                searchProductParam,
                response,
                offset,
                loadType == LoadType.REFRESH
            )

            MediatorResult.Success(
                endOfPaginationReached = response.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyLimitData(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                if (searchProductParam == null) remoteKeys?.nextKey?.minus(10)
                    ?: Constants.INITIAL_OFFSET_PAGE else 0
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uuid?.let { repoId ->
                localDataSource.getRemoteKeyById(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { product -> localDataSource.getRemoteKeyById(product.uuid) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ProductEntity>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { product -> localDataSource.getRemoteKeyById(product.uuid) }
    }
}
