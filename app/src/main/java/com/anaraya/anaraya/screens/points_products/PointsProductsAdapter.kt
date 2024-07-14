package com.anaraya.anaraya.screens.points_products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductBinding
import com.anaraya.anaraya.screens.home.interaction.ProductPointsInteractionListener
import com.anaraya.anaraya.screens.shared_data.ProductUiState


class PointsProductsAdapter(private val interactionListener: ProductPointsInteractionListener) :
    PagingDataAdapter<ProductUiState, PointsProductsAdapter.PointsProductViewHolder>(
        PointsProductsDiffUtil()
    ) {

    class PointsProductViewHolder(val itemProductUiStateBinding: LayoutItemProductBinding) :
        RecyclerView.ViewHolder(itemProductUiStateBinding.root) {
        fun bind(productUiState: ProductUiState) {
            itemProductUiStateBinding.item = productUiState
            itemProductUiStateBinding.executePendingBindings()
        }
    }

    class PointsProductsDiffUtil : DiffUtil.ItemCallback<ProductUiState>() {
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
    ): PointsProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PointsProductViewHolder(
            LayoutItemProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PointsProductViewHolder, position: Int) {
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