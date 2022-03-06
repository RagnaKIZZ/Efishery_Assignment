package com.amd.efishery.assignment.data.remote

import com.amd.efishery.assignment.data.remote.model.*
import com.amd.efishery.assignment.utils.Url
import retrofit2.http.*

interface ApiService {

    @GET(Url.LIST)
    suspend fun getListProduct(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ) : List<ProductItem>

    @GET(Url.LIST)
    suspend fun searchListProduct(
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