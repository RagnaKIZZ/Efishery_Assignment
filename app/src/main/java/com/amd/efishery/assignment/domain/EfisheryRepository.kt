package com.amd.efishery.assignment.domain

import androidx.paging.Pager
import androidx.paging.PagingSource
import com.amd.efishery.assignment.data.local.entity.ProductEntity

interface EfisheryRepository {

    suspend fun getProducts(): Pager<Int, ProductEntity>

}