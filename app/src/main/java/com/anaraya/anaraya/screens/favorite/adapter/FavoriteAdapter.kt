package com.anaraya.anaraya.screens.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemFavBinding
import com.anaraya.anaraya.screens.favorite.interaction.FavoriteInteraction
import com.anaraya.anaraya.screens.shared_data.ProductUiState
import com.chauthai.swipereveallayout.ViewBinderHelper


class FavoriteAdapter(private val favoriteInteraction: FavoriteInteraction) :
    ListAdapter<ProductUiState, FavoriteAdapter.FavoriteViewHolder>(
        FavoriteDiffUtil()
    ) {

    private val viewHelper = ViewBinderHelper()
    class FavoriteViewHolder(private val itemFavBinding: LayoutItemFavBinding) :
        RecyclerView.ViewHolder(itemFavBinding.root) {

        val layout = itemFavBinding.swipeRevealLayoutFav
        private val delLayout = itemFavBinding.delLayout
        val delBtn: ImageButton = delLayout.findViewById(R.id.btnDeleteFav)

        fun bind(productUiState: ProductUiState) {
            itemFavBinding.item = productUiState
            itemFavBinding.executePendingBindings()
        }
    }

    class FavoriteDiffUtil : DiffUtil.ItemCallback<ProductUiState>() {
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
    ): FavoriteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(
            LayoutItemFavBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        viewHelper.bind(holder.layout, getItem(position)!!.id.toString())
        holder.bind(getItem(position)!!)
        holder.delBtn.setOnClickListener {
            favoriteInteraction.onClickDelete(getItem(position)!!.id,position)
        }
    }
}