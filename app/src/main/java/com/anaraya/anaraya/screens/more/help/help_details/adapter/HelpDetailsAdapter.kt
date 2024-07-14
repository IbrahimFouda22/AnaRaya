package com.anaraya.anaraya.screens.more.help.help_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemHelpDetailsBinding
import com.anaraya.anaraya.screens.more.help.help_details.HelpDetailsUiStateData


class HelpDetailsAdapter() :
    ListAdapter<HelpDetailsUiStateData, HelpDetailsAdapter.HelpDetailsViewHolder>(
        HelpDetailsDiffUtil()
    ) {

    class HelpDetailsViewHolder(private val layoutItemHelpDetailsBinding: LayoutItemHelpDetailsBinding) :
        RecyclerView.ViewHolder(layoutItemHelpDetailsBinding.root) {

        fun bind(helpUiList: HelpDetailsUiStateData) {
            layoutItemHelpDetailsBinding.item = helpUiList
            layoutItemHelpDetailsBinding.executePendingBindings()
        }
    }

    class HelpDetailsDiffUtil : DiffUtil.ItemCallback<HelpDetailsUiStateData>() {
        override fun areItemsTheSame(oldItem: HelpDetailsUiStateData, newItem: HelpDetailsUiStateData): Boolean {
            return oldItem.problem == newItem.problem
        }

        override fun areContentsTheSame(oldItem: HelpDetailsUiStateData, newItem: HelpDetailsUiStateData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HelpDetailsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HelpDetailsViewHolder(
            LayoutItemHelpDetailsBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HelpDetailsViewHolder, position: Int) {
        holder.bind(getItem(position)!!)

    }

}