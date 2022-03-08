package com.amd.efishery.assignment.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.DeleteProductParams
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
import com.amd.efishery.assignment.domain.usecase.OptionAreaUseCase
import com.amd.efishery.assignment.domain.usecase.OptionSizesUseCase
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
    private val productUseCase: ProductUseCase,
    private val optionSizesUseCase: OptionSizesUseCase,
    private val optionAreaUseCase: OptionAreaUseCase
) : ViewModel() {

    private val _stateLoading = MutableStateFlow(value = false)
    val stateLoading = _stateLoading.asStateFlow()

    private val _stateActionProduct = MutableSharedFlow<Pair<Boolean, TypeProductAction>>()
    val stateActionProduct = _stateActionProduct.asSharedFlow()

    private val _stateQuerySearch = MutableStateFlow<SearchProductParam?>(value = null)

    private val _stateGetSize = MutableStateFlow<List<OptionSizeEntity>>(value = emptyList())
    val stateGetSize = _stateGetSize.asStateFlow()

    private val _stateGetProvince = MutableStateFlow<List<OptionAreaEntity>>(value = emptyList())
    val stateGetProvince = _stateGetProvince.asStateFlow()

    private val _stateGetCity = MutableStateFlow<List<OptionAreaEntity>>(value = emptyList())
    val stateGetCity = _stateGetCity.asStateFlow()

    init {
        getSizes()
        getArea()
    }

    private fun getSizes() = viewModelScope.launch {
        _stateGetSize.emit(optionSizesUseCase.getSizes())
    }

    private fun getArea() = viewModelScope.launch {
        _stateGetProvince.emit(optionAreaUseCase.getAreas())
    }

    fun getAreaByProvince(province: String) = viewModelScope.launch {
        _stateGetCity.emit(optionAreaUseCase.getAreasByProvince(province))
    }

    @ExperimentalCoroutinesApi
    val stateListProduct = _stateQuerySearch.flatMapLatest { param ->
        productUseCase.getProducts(param).flow
    }.cachedIn(viewModelScope)

    fun searchProduct(param: SearchProductParam?) = viewModelScope.launch {
        _stateQuerySearch.emit(param)
    }

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