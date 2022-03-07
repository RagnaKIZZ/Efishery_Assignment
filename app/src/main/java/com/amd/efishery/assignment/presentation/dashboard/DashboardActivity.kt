package com.amd.efishery.assignment.presentation.dashboard

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amd.efishery.assignment.databinding.ActivityDashboardBinding
import com.amd.efishery.assignment.presentation.dashboard.adapter.ProductAdapter
import com.amd.efishery.assignment.utils.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val viewModel: DashboardViewModel by viewModels()

    private val productAdapter by lazy { ProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUiState()
        pagingSetup()
        observer()
    }

    private fun observer() {
        lifecycleScope.launchWhenStarted {
            viewModel.getProduct().collectLatest {
                productAdapter.submitData(it)
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
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.error?.printStackTrace()
        }
    }
}