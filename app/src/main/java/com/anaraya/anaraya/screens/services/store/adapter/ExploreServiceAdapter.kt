package com.anaraya.anaraya.screens.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemExploreServiceBinding
import com.anaraya.anaraya.screens.services.store.service.explore_services.ExploreServiceUiDetails
import com.anaraya.anaraya.screens.services.store.interaction.ExploreServiceInteraction


class ExploreServiceAdapter(private val interactionListener: ExploreServiceInteraction) :
    PagingDataAdapter<ExploreServiceUiDetails, ExploreServiceAdapter.ExploreProductViewHolder>(
        ExploreProductDiffUtil()
    ) {

    class ExploreProductViewHolder(val layoutItemExploreServiceBinding: LayoutItemExploreServiceBinding ) :
        RecyclerView.ViewHolder(layoutItemExploreServiceBinding.root) {
        fun bind(productUiState: ExploreServiceUiDetails) {
            layoutItemExploreServiceBinding.item = productUiState
            layoutItemExploreServiceBinding.executePendingBindings()
        }
    }

    class ExploreProductDiffUtil : DiffUtil.ItemCallback<ExploreServiceUiDetails>() {
        override fun areItemsTheSame(oldItem: ExploreServiceUiDetails, newItem: ExploreServiceUiDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExploreServiceUiDetails, newItem: ExploreServiceUiDetails): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ExploreProductViewHolder(
            LayoutItemExploreServiceBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExploreProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.layoutItemExploreServiceBinding.clickExploreService.setOnClickListener {
            interactionListener.onClick(getItem(position)!!)
        }
    }

}