package com.amd.efishery.assignment.data.remote

import com.amd.efishery.assignment.data.remote.model.product.*
import com.amd.efishery.assignment.utils.Url
import retrofit2.http.*

interface ApiService {

    @GET(Url.LIST)
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : List<ProductItem>

    @GET(Url.LIST)
    suspend fun searchProducts(
        @Query("search") params: String
    ) : List<ProductItem>

    @POST(Url.LIST)
    suspend fun createProduct(
        @Body params: List<ProductItem>
    ) : SuccessCreate

    @DELETE(Url.LIST)
    suspend fun deleteProduct(
        @Body params: DeleteProductParams
    ) : SuccessDelete

    @PUT(Url.LIST)
    suspend fun updateProduct(
        @Body params: UpdateProductParams
    ) : SuccessUpdate

}