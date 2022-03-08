package com.amd.efishery.assignment.domain.usecase

import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
import com.amd.efishery.assignment.data.remote.model.product.UpdateProductParams
import com.amd.efishery.assignment.domain.EfisheryRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
   private val repository: EfisheryRepository
) {

    suspend fun getProducts(param: SearchProductParam?) = repository.getProducts(param)
    suspend fun insertProduct(body: ProductItem) = repository.insertProduct(body)
    suspend fun deleteProduct(uuid: String) = repository.deleteProduct(uuid)
    suspend fun updateProduct(body: ProductEntity) = repository.updateProduct(body)

}