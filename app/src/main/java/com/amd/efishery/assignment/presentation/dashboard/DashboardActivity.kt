package com.amd.efishery.assignment.presentation.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amd.efishery.assignment.R
import com.amd.efishery.assignment.data.local.entity.OptionAreaEntity
import com.amd.efishery.assignment.data.local.entity.OptionSizeEntity
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.data.remote.model.product.SearchProductParam
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
                    productAdapter.submitData(lifecycle, it)
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

            lifecycleScope.launchWhenResumed {
                stateGetSize.collectLatest {
                    setupSize(it)
                }
            }

            lifecycleScope.launchWhenResumed {
                stateGetProvince.collectLatest {
                    setupProvince(it)
                }
            }

            lifecycleScope.launchWhenResumed {
                stateGetCity.collectLatest {
                    setupCity(it)
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

    private fun setupSize(list: List<OptionSizeEntity>) {
        binding.edtSize.setAdapter(
            ArrayAdapter(this, R.layout.item_dropdown, list.map { it.size })
        )
    }

    private fun setupProvince(list: List<OptionAreaEntity>) {
        binding.edtProvince.setAdapter(
            ArrayAdapter(
                this,
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
                this,
                R.layout.item_dropdown,
                list.map { it.city.uppercase() }.toSet().toList()
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.rvProduct.scrollToPosition(0)
                viewModel.searchProduct(
                    if (newText.isNullOrEmpty()) null else SearchProductParam(
                        komoditas = newText
                    )
                )
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}