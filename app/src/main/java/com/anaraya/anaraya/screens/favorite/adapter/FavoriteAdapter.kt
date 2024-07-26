package com.anaraya.anaraya.screens.favorite.adapter

import android.annotation.SuppressLint
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
import com.google.android.material.checkbox.MaterialCheckBox


class FavoriteAdapter(private val favoriteInteraction: FavoriteInteraction) :
    ListAdapter<ProductUiState, FavoriteAdapter.FavoriteViewHolder>(
        FavoriteDiffUtil()
    ) {

    private val viewHelper = ViewBinderHelper()

    class FavoriteViewHolder(private val itemFavBinding: LayoutItemFavBinding) :
        RecyclerView.ViewHolder(itemFavBinding.root) {

        val layout = itemFavBinding.swipeRevealLayoutFav
        private val delLayout = itemFavBinding.delLayout
        private val dataLayout = itemFavBinding.dataLayout
        val delBtn: ImageButton = delLayout.findViewById(R.id.btnDeleteFav)
        val checkBox: MaterialCheckBox = dataLayout.findViewById(R.id.checkBoxAddToCart)

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
        viewType: Int,
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
            favoriteInteraction.onClickDelete(getItem(position)!!.id, position)
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            changeChecked(position)
            favoriteInteraction.onClickCheckBox(getItem(position).id, isChecked)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeChecked(position: Int) {
        getItem(position)!!.isSelectedInFavToAddToCart =
            !getItem(position)!!.isSelectedInFavToAddToCart
        notifyItemChanged(position)
        notifyDataSetChanged()
    }
}