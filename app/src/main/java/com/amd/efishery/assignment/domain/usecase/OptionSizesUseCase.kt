package com.amd.efishery.assignment.domain.usecase

import com.amd.efishery.assignment.domain.EfisheryRepository
import javax.inject.Inject

class OptionSizesUseCase @Inject constructor(
    private val repository: EfisheryRepository
) {

    suspend fun getSizes() = repository.getSizes()

}