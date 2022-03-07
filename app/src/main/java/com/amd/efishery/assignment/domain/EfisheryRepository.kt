package com.amd.efishery.assignment.domain

import androidx.paging.Pager
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.ProductItem

interface EfisheryRepository {

    suspend fun getProducts(): Pager<Int, ProductEntity>
    suspend fun insertProduct(body: ProductItem): Boolean
    suspend fun deleteProduct(uuid: String): Boolean
    suspend fun getSizes(): List<OptionSizeEntity>
    suspend fun getAreas(): List<OptionAreaEntity>
    suspend fun getAreaByProvince(province: String): List<OptionAreaEntity>
}