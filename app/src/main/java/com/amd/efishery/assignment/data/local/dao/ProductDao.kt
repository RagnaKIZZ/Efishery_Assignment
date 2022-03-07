package com.amd.efishery.assignment.data.local.dao

import androidx.paging.PagingSource
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

//    @Query("SELECT * FROM productentity ORDER BY timestamp ASC")
    @Query("SELECT * FROM productentity")
    fun getProductListPaged() : PagingSource<Int, ProductEntity>

    @Query("SELECT COUNT(*) FROM productentity")
    fun getTotalItem() : Int

    @Delete
    fun deleteProduct(product: ProductEntity)

    @Delete
    fun deleteProduct(products: List<ProductEntity>)

    @Query("DELETE FROM productentity WHERE uuid = :id")
    fun deleteProduct(id: String)

    @Query("DELETE FROM productentity")
    fun deleteProduct()

}