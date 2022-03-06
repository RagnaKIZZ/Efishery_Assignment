package com.amd.efishery.assignment.data.local.dao

import androidx.room.*
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertProduct(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertProduct(product: ProductEntity)

    @Query("SELECT * FROM productentity")
    fun getProductList() : List<ProductEntity>

    @Query("SELECT * FROM productentity")
    fun getProductListFlow() : Flow<List<ProductEntity>>

    @Delete
    fun deleteProduct(product: ProductEntity)

    @Delete
    fun deleteProduct(products: List<ProductEntity>)

}