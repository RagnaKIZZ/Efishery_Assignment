package com.amd.efishery.assignment.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.DeleteProductParams
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
import com.amd.efishery.assignment.domain.usecase.ProductUseCase
import com.amd.efishery.assignment.utils.TypeProductAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _stateLoading = MutableStateFlow(value = false)
    val stateLoading = _stateLoading.asStateFlow()

    private val _stateActionProduct = MutableSharedFlow<Pair<Boolean, TypeProductAction>>()
    val stateActionProduct = _stateActionProduct.asSharedFlow()

    private val _stateQuerySearch = MutableStateFlow<SearchProductParam?>(value = null)

    @ExperimentalCoroutinesApi
    val stateListProduct = _stateQuerySearch.flatMapLatest { param ->
        productUseCase.getProducts(param).flow
    }.cachedIn(viewModelScope)

//    fun getProduct() = productUseCase.getProducts().flow.cachedIn(viewModelScope)

    fun createProduct(body: ProductItem) = viewModelScope.launch {
        _stateLoading.emit(true)
        _stateActionProduct.emit(Pair(productUseCase.insertProduct(body), TypeProductAction.CREATE))
            .also {
                _stateLoading.emit(false)
            }
    }

    fun deleteProduct(uuid: String) = viewModelScope.launch {
        _stateLoading.emit(true)
        _stateActionProduct.emit(Pair(productUseCase.deleteProduct(uuid), TypeProductAction.DELETE))
            .also {
                _stateLoading.emit(false)
            }
    }

    fun updateProduct(body: ProductEntity) = viewModelScope.launch {
        _stateLoading.emit(true)
        _stateActionProduct.emit(Pair(productUseCase.updateProduct(body), TypeProductAction.UPDATE))
            .also {
                _stateLoading.emit(false)
            }
    }

}