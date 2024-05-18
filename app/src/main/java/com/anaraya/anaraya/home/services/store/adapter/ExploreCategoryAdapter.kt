package com.anaraya.anaraya.home.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemExploreBinding
import com.anaraya.anaraya.home.home.CategoryUiState


class ExploreCategoryAdapter :
    ListAdapter<CategoryUiState, ExploreCategoryAdapter.HomeCategoryViewHolder>(
        HomeCategoryDiffUtil()
    ) {

    class HomeCategoryViewHolder(private val layoutItemExploreBinding: LayoutItemExploreBinding) :
        RecyclerView.ViewHolder(layoutItemExploreBinding.root) {
        fun bind(categoryUiState: CategoryUiState) {
            layoutItemExploreBinding.item = categoryUiState
            layoutItemExploreBinding.executePendingBindings()
        }
    }

    class HomeCategoryDiffUtil : DiffUtil.ItemCallback<CategoryUiState>() {
        override fun areItemsTheSame(oldItem: CategoryUiState, newItem: CategoryUiState): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CategoryUiState, newItem: CategoryUiState): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeCategoryViewHolder(
            LayoutItemExploreBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
//        holder.itemView.setOnClickListener {
//            interaction.onClickItem(getItem(position)!!.id,getItem(position)!!.name)
//        }
    }
}