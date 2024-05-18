package com.anaraya.anaraya.home.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemItemStoreDataBinding
import com.anaraya.anaraya.home.services.store.interaction.ItemsServiceInteraction
import com.anaraya.anaraya.home.services.store.my_items.ProductStoreItemState


class StoreProductItemServiceAdapter(private val itemsServiceInteraction: ItemsServiceInteraction) :
    PagingDataAdapter<ProductStoreItemState, StoreProductItemServiceAdapter.HomeTrendingProductViewHolder>(
        HomeTrendingProductDiffUtil()
    ) {

    class HomeTrendingProductViewHolder(val itemItemStoreBinding: LayoutItemItemStoreDataBinding) :
        RecyclerView.ViewHolder(itemItemStoreBinding.root) {
        fun bind(productStoreItemState: ProductStoreItemState) {
            itemItemStoreBinding.item = productStoreItemState
            itemItemStoreBinding.executePendingBindings()
        }
    }

    class HomeTrendingProductDiffUtil : DiffUtil.ItemCallback<ProductStoreItemState>() {
        override fun areItemsTheSame(oldItem: ProductStoreItemState, newItem: ProductStoreItemState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductStoreItemState, newItem: ProductStoreItemState): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeTrendingProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeTrendingProductViewHolder(
            LayoutItemItemStoreDataBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeTrendingProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemItemStoreBinding.consDataStore.setOnClickListener {
            itemsServiceInteraction.onClick(getItem(position),getItem(position)!!.id , getItem(position)!!.userAction)
        }
//        holder.itemProductUiStateBinding.btnAddProductItem.setOnClickListener {
//            interactionListener.addToCart(getItem(position)!!.id,position)
//        }

    }
}