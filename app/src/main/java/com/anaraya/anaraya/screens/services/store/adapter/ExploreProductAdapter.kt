package com.anaraya.anaraya.screens.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemExploreProductBinding
import com.anaraya.anaraya.screens.services.store.interaction.ExploreProductInteraction
import com.anaraya.anaraya.screens.services.store.product.explore.explore_products.ExploreProductUiDetails


class ExploreProductAdapter(private val interactionListener: ExploreProductInteraction) :
    PagingDataAdapter<ExploreProductUiDetails, ExploreProductAdapter.ExploreProductViewHolder>(
        ExploreProductDiffUtil()
    ) {

    class ExploreProductViewHolder(val layoutItemExploreProductBinding: LayoutItemExploreProductBinding ) :
        RecyclerView.ViewHolder(layoutItemExploreProductBinding.root) {
        fun bind(productUiState: ExploreProductUiDetails) {
            layoutItemExploreProductBinding.item = productUiState
            layoutItemExploreProductBinding.executePendingBindings()
        }
    }

    class ExploreProductDiffUtil : DiffUtil.ItemCallback<ExploreProductUiDetails>() {
        override fun areItemsTheSame(oldItem: ExploreProductUiDetails, newItem: ExploreProductUiDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExploreProductUiDetails, newItem: ExploreProductUiDetails): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ExploreProductViewHolder(
            LayoutItemExploreProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExploreProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.layoutItemExploreProductBinding.clickExploreProduct.setOnClickListener {
            interactionListener.onClick(getItem(position)!!)
        }
    }

}