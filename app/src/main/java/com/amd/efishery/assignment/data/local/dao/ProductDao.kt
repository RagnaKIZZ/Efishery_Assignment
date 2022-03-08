package com.amd.efishery.assignment.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity)

    @Query("SELECT * FROM productentity")
    suspend fun getProductList(): List<ProductEntity>

    @Query("SELECT * FROM productentity")
    fun getProductListFlow(): Flow<List<ProductEntity>>

    //    @Query("SELECT * FROM productentity ORDER BY timestamp ASC")
    @Query("SELECT * FROM productentity")
    fun getProductListPaged(): PagingSource<Int, ProductEntity>

    @Query("SELECT COUNT(*) FROM productentity")
    suspend fun getTotalItem(): Int

    @Query(
        "UPDATE productEntity SET komoditas = :name, size = :size, price = :price, areaProvinsi = :province," +
                " areaKota = :city, tglParsed = :tglParsed, timestamp = :timeStamp WHERE uuid = :uuid "
    )
    suspend fun updateProduct(
        uuid: String,
        name: String,
        size: String,
        price: String,
        province: String,
        city: String,
        tglParsed: String,
        timeStamp: String
    )

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(products: List<ProductEntity>)

    @Query("DELETE FROM productentity WHERE uuid = :id")
    suspend fun deleteProduct(id: String)

    @Query("DELETE FROM productentity WHERE komoditas LIKE :name")
    suspend fun deleteProductByName(name: String)

    @Query("DELETE FROM productentity WHERE size = :size")
    suspend fun deleteProductBySize(size: String)

    @Query("DELETE FROM productentity WHERE areaProvinsi = :province")
    suspend fun deleteProductByProvince(province: String)

    @Query("DELETE FROM productentity WHERE areaKota = :city")
    suspend fun deleteProductByCity(city: String)

    @Query("DELETE FROM productentity")
    suspend fun deleteProduct()

}