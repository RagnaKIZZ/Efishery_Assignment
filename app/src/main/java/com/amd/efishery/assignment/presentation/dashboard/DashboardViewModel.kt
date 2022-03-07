package com.amd.efishery.assignment.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.amd.efishery.assignment.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    suspend fun getProduct() = productUseCase.getProducts().cachedIn(viewModelScope)

}