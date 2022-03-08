package com.amd.efishery.assignment.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amd.efishery.assignment.data.local.entity.ProductEntity
import com.amd.efishery.assignment.databinding.ItemProductBinding
import com.amd.efishery.assignment.utils.toRp

class ProductAdapter(
    private val onDetailItem: ((ProductEntity) -> Unit)? = null,
    private val onDeleteItem: ((ProductEntity) -> Unit)? = null
) :
    PagingDataAdapter<ProductEntity, ProductAdapter.ViewHolder>(DiffUtilCallBack) {

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binds(item: ProductEntity) {
            with(binding) {
                txtProductName.text = item.komoditas
                txtProductSize.text = item.size + "Kg"
                txtProductPrice.text = item.price?.toRp()
                txtProductProvince.text = item.areaKota
                btnDelete.setOnClickListener {
                    onDeleteItem?.invoke(item)
                }
                root.setOnClickListener {
                    onDetailItem?.invoke(item)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binds(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    object DiffUtilCallBack : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(
            oldItem: ProductEntity,
            newItem: ProductEntity
        ): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(
            oldItem: ProductEntity,
            newItem: ProductEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

}