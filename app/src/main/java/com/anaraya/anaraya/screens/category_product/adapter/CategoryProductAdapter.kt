package com.anaraya.anaraya.screens.category_product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductCatSearchBinding
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import com.anaraya.anaraya.screens.shared_interaction.ProductInteractionListener


class CategoryProductAdapter(private val interactionListener: ProductInteractionListener) :
    PagingDataAdapter<ProductUiState, CategoryProductAdapter.HomeTrendingProductViewHolder>(
        HomeTrendingProductDiffUtil()
    ) {

    class HomeTrendingProductViewHolder(val itemProductUiStateBinding: LayoutItemProductCatSearchBinding) :
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
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeTrendingProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeTrendingProductViewHolder(
            LayoutItemProductCatSearchBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeTrendingProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            interactionListener.onCLick(getItem(position)!!.id)
        }
        holder.itemProductUiStateBinding.btnAddProductItem.setOnClickListener {
            interactionListener.addToCart(getItem(position)!!.id,position)
        }

    }

    fun changeIcon(position: Int){
        getItem(position)!!.inBasket = !getItem(position)!!.inBasket
    }
}