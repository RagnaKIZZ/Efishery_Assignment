package com.amd.efishery.assignment.domain.usecase

import com.amd.efishery.assignment.domain.EfisheryRepository
import javax.inject.Inject

class OptionAreaUseCase @Inject constructor(
    private val repository: EfisheryRepository
) {

    suspend fun getAreas() = repository.getAreas()
    suspend fun getAreasByProvince(province: String) = repository.getAreaByProvince(province)

}