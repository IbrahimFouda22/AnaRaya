package com.anaraya.anaraya.screens.more.help.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemHelpBinding
import com.anaraya.anaraya.screens.more.help.HelpUiStateData
import com.anaraya.anaraya.screens.more.help.interaction.HelpInteraction


class HelpAdapter(private val helpInteraction: HelpInteraction) :
    ListAdapter<HelpUiStateData, HelpAdapter.HelpViewHolder>(
        HelpDiffUtil()
    ) {

    class HelpViewHolder(private val layoutItemHelpBinding: LayoutItemHelpBinding) :
        RecyclerView.ViewHolder(layoutItemHelpBinding.root) {

        fun bind(helpUiList: HelpUiStateData) {
            layoutItemHelpBinding.item = helpUiList
            layoutItemHelpBinding.executePendingBindings()
        }
    }

    class HelpDiffUtil : DiffUtil.ItemCallback<HelpUiStateData>() {
        override fun areItemsTheSame(oldItem: HelpUiStateData, newItem: HelpUiStateData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HelpUiStateData, newItem: HelpUiStateData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HelpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HelpViewHolder(
            LayoutItemHelpBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            helpInteraction.onClick(getItem(position))
        }
    }

}