package com.amd.efishery.assignment.domain

import com.amd.efishery.assignment.data.local.entity.ProductEntity

interface ProductRepository {

   suspend fun getProducts(limit: Int, offset: Int) : List<ProductEntity>

}