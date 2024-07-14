package com.anaraya.anaraya.screens.services.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemExploreBinding
import com.anaraya.anaraya.screens.home.CategoryUiState
import com.anaraya.anaraya.screens.services.store.interaction.ExploreCategoryInteraction


class ExploreCategoryAdapter(private val interaction: ExploreCategoryInteraction) :
    ListAdapter<CategoryUiState, ExploreCategoryAdapter.HomeCategoryViewHolder>(
        HomeCategoryDiffUtil()
    ) {

    class HomeCategoryViewHolder(val layoutItemExploreBinding: LayoutItemExploreBinding) :
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

        holder.layoutItemExploreBinding.clickExplore.setOnClickListener {
            interaction.onClick(getItem(position)!!.id, getItem(position)!!.name)
        }
    }
}