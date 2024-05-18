package com.anaraya.anaraya.home.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemProductBinding
import com.anaraya.anaraya.home.shared_data.ProductUiState
import com.anaraya.anaraya.home.shared_interaction.ProductInteractionListener


class ProductDetailsRecommendedAdapter(private val interactionListener: ProductInteractionListener) :
    ListAdapter<ProductUiState, ProductDetailsRecommendedAdapter.ProductDetailsRecommendedViewHolder>(
        ProductDetailsRecommendedDiffUtil()
    ) {

    class ProductDetailsRecommendedViewHolder(val itemProductUiStateBinding: LayoutItemProductBinding) :
        RecyclerView.ViewHolder(itemProductUiStateBinding.root) {
        fun bind(productUiState: ProductUiState) {
            itemProductUiStateBinding.item = productUiState
            itemProductUiStateBinding.executePendingBindings()
        }
    }

    class ProductDetailsRecommendedDiffUtil : DiffUtil.ItemCallback<ProductUiState>() {
        override fun areItemsTheSame(oldItem: ProductUiState, newItem: ProductUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUiState, newItem: ProductUiState): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailsRecommendedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductDetailsRecommendedViewHolder(
            LayoutItemProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductDetailsRecommendedViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            interactionListener.onCLick(getItem(position)!!.id)
        }
        holder.itemProductUiStateBinding.btnAddProductItem.setOnClickListener {
            interactionListener.addToCart(getItem(position)!!.id, position)
        }
    }

    fun changeIcon(position: Int){
        getItem(position)!!.inBasket = !getItem(position)!!.inBasket
        notifyItemChanged(position)
    }


}