package com.anaraya.anaraya.home.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemCategoryBinding
import com.anaraya.anaraya.home.home.CategoryUiState
import com.anaraya.anaraya.home.shared_interaction.CategoryInteraction


class CategoryAdapter(private val categoryInteraction: CategoryInteraction) :
    PagingDataAdapter<CategoryUiState, CategoryAdapter.HomeCategoryViewHolder>(
        HomeCategoryDiffUtil()
    ) {

    class HomeCategoryViewHolder(private val layoutItemCategoryBinding: LayoutItemCategoryBinding) :
        RecyclerView.ViewHolder(layoutItemCategoryBinding.root) {
        fun bind(categoryUiState: CategoryUiState) {
            layoutItemCategoryBinding.item = categoryUiState
            layoutItemCategoryBinding.executePendingBindings()
        }
    }

    class HomeCategoryDiffUtil : DiffUtil.ItemCallback<CategoryUiState>() {
        override fun areItemsTheSame(oldItem: CategoryUiState, newItem: CategoryUiState): Boolean {
            return oldItem.image == newItem.image
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
            LayoutItemCategoryBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            categoryInteraction.onClickItem(getItem(position)!!.id,getItem(position)!!.name)
        }
    }
}