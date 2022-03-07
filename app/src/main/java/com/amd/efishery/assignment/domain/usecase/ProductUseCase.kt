package com.amd.efishery.assignment.domain.usecase

import com.amd.efishery.assignment.domain.EfisheryRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
   private val repository: EfisheryRepository
) {

    suspend fun getProducts() = repository.getProducts().flow

}