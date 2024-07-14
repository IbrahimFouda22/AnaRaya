package com.anaraya.anaraya.screens.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductAdBinding
import com.anaraya.anaraya.screens.home.interaction.HomeAdsInteraction
import com.anaraya.anaraya.screens.home.ProductAdUiState


class HomeAdsAdapter(private val homeAdsInteraction: HomeAdsInteraction) :
    ListAdapter<ProductAdUiState, HomeAdsAdapter.HomeAdsViewHolder>(
        HomeAdsDiffUtil()
    ) {
    class HomeAdsViewHolder(private val layoutItemProductAdBinding: LayoutItemProductAdBinding) :
        RecyclerView.ViewHolder(layoutItemProductAdBinding.root) {
        fun bind(productAdUiState: ProductAdUiState) {
            layoutItemProductAdBinding.product = productAdUiState
        }
    }

    class HomeAdsDiffUtil : DiffUtil.ItemCallback<ProductAdUiState>() {
        override fun areItemsTheSame(
            oldItem: ProductAdUiState,
            newItem: ProductAdUiState
        ): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(
            oldItem: ProductAdUiState,
            newItem: ProductAdUiState
        ): Boolean {
            return oldItem.image == newItem.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeAdsViewHolder(LayoutItemProductAdBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAdsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            homeAdsInteraction.adClick(getItem(position).id,getItem(position).adsType,getItem(position).adsLink)
        }
    }
}
