package com.anaraya.anaraya.screens.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductAdBinding


class ProductDetailsAdapter: ListAdapter<ProductDetailsUiImages,ProductDetailsAdapter.HomeAdsViewHolder>(HomeAdsDiffUtil()) {
    class HomeAdsViewHolder(private val layoutItemProductAdBinding: LayoutItemProductAdBinding) :RecyclerView.ViewHolder(layoutItemProductAdBinding.root){
        fun bind(productDetailsUiImage: ProductDetailsUiImages){
            layoutItemProductAdBinding.product = productDetailsUiImage
        }
    }

    class HomeAdsDiffUtil: DiffUtil.ItemCallback<ProductDetailsUiImages>() {
        override fun areItemsTheSame(
            oldItem: ProductDetailsUiImages,
            newItem: ProductDetailsUiImages
        ): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(
            oldItem: ProductDetailsUiImages,
            newItem: ProductDetailsUiImages
        ): Boolean {
            return oldItem.image == newItem.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeAdsViewHolder(LayoutItemProductAdBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: HomeAdsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
