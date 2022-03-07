package com.amd.efishery.assignment.presentation.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.domain.usecase.OptionAreaUseCase
import com.amd.efishery.assignment.domain.usecase.OptionSizesUseCase
import com.amd.efishery.assignment.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetAddProductViewModel @Inject constructor(
    private val optionSizesUseCase: OptionSizesUseCase,
    private val optionAreaUseCase: OptionAreaUseCase
) : ViewModel() {

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

}