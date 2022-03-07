package com.amd.efishery.assignment.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.amd.efishery.assignment.data.local.LocalDataSource
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.RemoteDataSource
import com.amd.efishery.assignment.data.remote.model.product.Condition
import com.amd.efishery.assignment.data.remote.model.product.DeleteProductParams
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.product.SuccessCreate
import com.amd.efishery.assignment.di.DispatcherThread
import com.amd.efishery.assignment.domain.EfisheryRepository
import com.amd.efishery.assignment.domain.paging.ProductMediator
import com.amd.efishery.assignment.utils.NetworkAwareHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EfisheryRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val networkHandle: NetworkAwareHandler,
    private val dispatcher: DispatcherThread
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

    override suspend fun insertProduct(body: ProductItem): Boolean {
        return remoteDataSource.insertProduct(body)
    }

    override suspend fun deleteProduct(uuid: String): Boolean {
        return if (remoteDataSource.deleteProduct(
                DeleteProductParams(
                    Condition(
                        uuid = uuid
                    ),
                    limit = 1
                )
            )
        ) {
            localDataSource.deleteProductItem(uuid)
            true
        } else {
            false
        }
    }

    override suspend fun getSizes(): List<OptionSizeEntity> {
        return if (networkHandle.isOnline()) {
            localDataSource.insertSize(remoteDataSource.getSizes())
            localDataSource.getSizes()
        } else {
            localDataSource.getSizes()
        }
    }

    override suspend fun getAreas(): List<OptionAreaEntity> {
        return if (networkHandle.isOnline()) {
            localDataSource.insertArea(remoteDataSource.getAreas())
            localDataSource.getArea()
        } else {
            localDataSource.getArea()
        }
    }

    override suspend fun getAreaByProvince(province: String): List<OptionAreaEntity> {
        return localDataSource.getAreaByProvince(province)
    }
}