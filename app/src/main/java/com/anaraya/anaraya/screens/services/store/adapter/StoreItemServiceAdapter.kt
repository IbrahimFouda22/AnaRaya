package com.anaraya.anaraya.screens.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemItemStoreServiceDataBinding
import com.anaraya.anaraya.screens.services.store.interaction.ItemsServicesInteraction
import com.anaraya.anaraya.screens.services.store.service.my_items.ProductStoreItemServiceState


class StoreItemServiceAdapter(private val itemsServicesInteraction: ItemsServicesInteraction) :
    PagingDataAdapter<ProductStoreItemServiceState, StoreItemServiceAdapter.HomeTrendingProductViewHolder>(
        HomeTrendingProductDiffUtil()
    ) {

    class HomeTrendingProductViewHolder(val itemItemStoreBinding: LayoutItemItemStoreServiceDataBinding) :
        RecyclerView.ViewHolder(itemItemStoreBinding.root) {
        fun bind(productStoreItemState: ProductStoreItemServiceState) {
            itemItemStoreBinding.item = productStoreItemState
            itemItemStoreBinding.executePendingBindings()
        }
    }

    class HomeTrendingProductDiffUtil : DiffUtil.ItemCallback<ProductStoreItemServiceState>() {
        override fun areItemsTheSame(oldItem: ProductStoreItemServiceState, newItem: ProductStoreItemServiceState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductStoreItemServiceState, newItem: ProductStoreItemServiceState): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeTrendingProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeTrendingProductViewHolder(
            LayoutItemItemStoreServiceDataBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeTrendingProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemItemStoreBinding.consDataStore.setOnClickListener {
            itemsServicesInteraction.onClick(getItem(position)!!.id , getItem(position)!!.status)
        }
//        holder.itemProductUiStateBinding.btnAddProductItem.setOnClickListener {
//            interactionListener.addToCart(getItem(position)!!.id,position)
//        }

    }
}