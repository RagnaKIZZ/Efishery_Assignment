package com.amd.efishery.assignment.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.amd.efishery.assignment.R
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.remote.model.product.ProductItem
import com.amd.efishery.assignment.databinding.BottomSheetAddProductBinding
import com.amd.efishery.assignment.utils.listenError
import com.amd.efishery.assignment.utils.validateField
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BottomSheetAddProduct(
    private val onCreateOrder: ((ProductItem) -> Unit)? = null
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BottomSheetAddProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialog()
        setupObserver()
        setupUi()
    }

    private fun setupUi() {
        with(binding) {
            edtCity.listenError()
            edtProductName.listenError()
            edtProductPrice.listenError()
            edtSize.listenError()
            btnAddProduct.setOnClickListener {
                createProduct()
            }
        }
    }

    private fun setupObserver() {
        viewModel.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                stateGetSize.collectLatest {
                    setupSize(it)
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                stateGetProvince.collectLatest {
                    setupProvince(it)
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                stateGetCity.collectLatest {
                    setupCity(it)
                }
            }

        }
    }

    private fun setDialog() {
        if (dialog is BottomSheetDialog) {
            with(dialog as BottomSheetDialog) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                setCancelable(true)
                setCanceledOnTouchOutside(true)

                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (behavior.state) {
                            BottomSheetBehavior.STATE_EXPANDED -> {

                            }
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                dismiss()
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {

                            }
                            else -> Unit

                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }

                })
            }
        }
    }

    private fun setupSize(list: List<OptionSizeEntity>) {
        binding.edtSize.setAdapter(
            ArrayAdapter(requireContext(), R.layout.item_dropdown, list.map { it.size })
        )
    }

    private fun setupProvince(list: List<OptionAreaEntity>) {
        binding.edtProvince.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown,
                list.map { it.province?.uppercase() }.toSet().toList()
            )
        )
        binding.edtProvince.addTextChangedListener {
            binding.edtCity.setText("")
            binding.edtProvince.error = null
            viewModel.getAreaByProvince(it.toString())
        }
    }

    private fun setupCity(list: List<OptionAreaEntity>) {
        binding.edtCity.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown,
                list.map { it.city.uppercase() }.toSet().toList()
            )
        )
    }

    private fun createProduct() {
        with(binding) {
            edtProductName.validateField()
            edtSize.validateField()
            edtProductPrice.validateField()
            edtProvince.validateField()
            edtCity.validateField()

            onCreateOrder?.invoke(
                ProductItem(
                    uuid = UUID.randomUUID().toString(),
                    komoditas = edtProductName.text.toString(),
                    size = edtSize.text.toString(),
                    price = edtProductPrice.text.toString(),
                    areaProvinsi = edtProvince.text.toString(),
                    areaKota = edtCity.text.toString(),
                    tglParsed = SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'.000Z'",
                        Locale.US
                    ).format(Date()),
                    timestamp = Date().time.toString()
                )
            ).also { dismiss() }
        }
    }

}