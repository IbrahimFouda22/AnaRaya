package com.anaraya.anaraya.screens.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductBinding
import com.anaraya.anaraya.screens.home.interaction.ProductPointsInteractionListener
import com.anaraya.anaraya.screens.shared_data.ProductUiState


class HomePointsProductAdapter(private val interactionListener: ProductPointsInteractionListener) :
    PagingDataAdapter<ProductUiState, HomePointsProductAdapter.HomeTrendingProductViewHolder>(
        HomeTrendingProductDiffUtil()
    ) {

    class HomeTrendingProductViewHolder(val itemProductUiStateBinding: LayoutItemProductBinding) :
        RecyclerView.ViewHolder(itemProductUiStateBinding.root) {
        fun bind(productUiState: ProductUiState) {
            itemProductUiStateBinding.item = productUiState
            itemProductUiStateBinding.executePendingBindings()
        }
    }

    class HomeTrendingProductDiffUtil : DiffUtil.ItemCallback<ProductUiState>() {
        override fun areItemsTheSame(oldItem: ProductUiState, newItem: ProductUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUiState, newItem: ProductUiState): Boolean {
            return oldItem.inBasket == newItem.inBasket
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeTrendingProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeTrendingProductViewHolder(
            LayoutItemProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeTrendingProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            interactionListener.onCLickPointProduct(getItem(position)!!.id)
        }
        holder.itemProductUiStateBinding.btnAddProductItem.setOnClickListener {
            interactionListener.addToCartPointProduct(getItem(position)!!.id, position)
        }
    }

    fun changeIcon(position: Int){
        getItem(position)!!.inBasket = !getItem(position)!!.inBasket
        notifyItemChanged(position)
    }
}