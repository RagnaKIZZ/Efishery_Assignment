package com.amd.efishery.assignment.data.remote

import com.amd.efishery.assignment.data.remote.model.area.OptionArea
import com.amd.efishery.assignment.data.remote.model.product.*
import com.amd.efishery.assignment.data.remote.model.size.OptionSize
import com.amd.efishery.assignment.utils.Url
import retrofit2.http.*

interface ApiService {

    //product
    @GET(Url.LIST)
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") params: String
    ): List<ProductItem>

    @GET(Url.LIST)
    suspend fun searchProducts(
        @Query("search") params: String
    ): List<ProductItem>

    @POST(Url.LIST)
    suspend fun createProduct(
        @Body params: List<ProductItem>
    ): SuccessCreate

    @HTTP(method = "DELETE", path = Url.LIST, hasBody = true)
    suspend fun deleteProduct(
        @Body params: DeleteProductParams
    ): SuccessDelete

    @PUT(Url.LIST)
    suspend fun updateProduct(
        @Body params: UpdateProductParams
    ): SuccessUpdate

    //sizes
    @GET(Url.OPTION_SIZE)
    suspend fun getSizes(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<OptionSize>

    //area
    @GET(Url.OPTION_AREA)
    suspend fun getAreas(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<OptionArea>

}