package com.amd.efishery.assignment.presentation.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amd.efishery.assignment.R
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.databinding.ActivityDashboardBinding
import com.amd.efishery.assignment.domain.mapper.toEntity
import com.amd.efishery.assignment.presentation.dashboard.adapter.ProductAdapter
import com.amd.efishery.assignment.presentation.dialog.BottomSheetAddProduct
import com.amd.efishery.assignment.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val viewModel: DashboardViewModel by viewModels()

    private val productAdapter by lazy {
        ProductAdapter(
            {
                updateItem(it)
            },
            {
                deleteItem(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUiState()
        setupUi()
        pagingSetup()
        observer()
    }

    private fun setupUi() {
        binding.btnAddProduct.setOnClickListener {
            showDialogProduct(true)
        }
        binding.swipeRefresh.setOnRefreshListener {
            productAdapter.refresh()
        }
    }

    private fun observer() {
        viewModel.apply {
            lifecycleScope.launchWhenStarted {
                viewModel.stateListProduct.collectLatest {
                    productAdapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                stateLoading.collectLatest {
                    binding.linearProgressBar.isVisible = it
                }
            }

            lifecycleScope.launch {
                stateActionProduct.collectLatest {
                    if (it.first && it.second != TypeProductAction.DELETE) {
                        productAdapter.refresh()
                    }
                    showToast(messageSuccessAction(it))
                }
            }
        }
    }

    private fun pagingSetup() {
        with(binding) {
            rvProduct.adapter = productAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(productAdapter),
                footer = PagingLoadStateAdapter(productAdapter)
            )
            lifecycleScope.launchWhenStarted {
                productAdapter.loadStateFlow.distinctUntilChangedBy { loadState -> loadState.refresh }
                    .filter { loadState -> loadState.refresh is LoadState.NotLoading }
                    .collect {
                        rvProduct.scrollToPosition(0)
                    }
            }
            rvProduct.layoutManager = LinearLayoutManager(this@DashboardActivity)
        }
    }

    private fun setupUiState() {
        productAdapter.addLoadStateListener { loadState ->
            binding.swipeRefresh.isRefreshing =
                loadState.source.refresh is LoadState.Loading && productAdapter.itemCount == 0
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.error?.printStackTrace()
        }
    }

    private fun deleteItem(productEntity: ProductEntity) {
        showAlertDialog(
            "Hapus Produk",
            "Apa Anda yakin ingin menghapus ${productEntity.komoditas}?"
        ) {
            viewModel.deleteProduct(productEntity.uuid)
        }
    }

    private fun updateItem(productEntity: ProductEntity) {
        showDialogProduct(false, productEntity)
    }

    private fun showDialogProduct(isCreate: Boolean, productEntity: ProductEntity? = null) {
        BottomSheetAddProduct(productEntity) { item ->
            logging(item.toString())
            if (isCreate) {
                viewModel.createProduct(item)
            } else {
                viewModel.updateProduct(item.toEntity())
            }
        }.show(supportFragmentManager, Constants.BOTTOMSHEET_PRODUCT_ADD)
    }

    private fun messageSuccessAction(param: Pair<Boolean, TypeProductAction>): String {
        return when {
            param.first && param.second == TypeProductAction.CREATE -> getString(R.string.success_add_data)
            param.first && param.second == TypeProductAction.DELETE -> getString(R.string.success_delete_product)
            param.first && param.second == TypeProductAction.UPDATE -> getString(R.string.success_update_product)
            !param.first && param.second == TypeProductAction.CREATE -> getString(R.string.failed_to_added_product)
            !param.first && param.second == TypeProductAction.DELETE -> getString(R.string.failed_to_delete_product)
            !param.first && param.second == TypeProductAction.UPDATE -> getString(R.string.failed_to_update_product)
            else -> ""
        }
    }
}