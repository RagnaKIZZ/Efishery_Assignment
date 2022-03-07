package com.amd.efishery.assignment.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity)

    @Query("SELECT * FROM productentity")
    suspend fun getProductList() : List<ProductEntity>

    @Query("SELECT * FROM productentity")
    fun getProductListFlow() : Flow<List<ProductEntity>>

//    @Query("SELECT * FROM productentity ORDER BY timestamp ASC")
    @Query("SELECT * FROM productentity")
    fun getProductListPaged() : PagingSource<Int, ProductEntity>

    @Query("SELECT COUNT(*) FROM productentity")
    suspend fun getTotalItem() : Int

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(products: List<ProductEntity>)

    @Query("DELETE FROM productentity WHERE uuid = :id")
    suspend fun deleteProduct(id: String)

    @Query("DELETE FROM productentity")
    suspend fun deleteProduct()

}