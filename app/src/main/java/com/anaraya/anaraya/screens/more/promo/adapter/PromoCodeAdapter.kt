package com.anaraya.anaraya.screens.more.promo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemPromoBinding
import com.anaraya.anaraya.screens.more.promo.PromoCodeUiItem
import com.anaraya.anaraya.screens.more.promo.interaction.PromoInteraction


class PromoCodeAdapter(private val promoInteraction: PromoInteraction) :
    ListAdapter<PromoCodeUiItem, PromoCodeAdapter.PromoCodeViewHolder>(
        PromoCodeDiffUtil()
    ) {

    class PromoCodeViewHolder(private val itemPromoBinding: LayoutItemPromoBinding) :
        RecyclerView.ViewHolder(itemPromoBinding.root) {

        fun bind(promoCodeUiItem: PromoCodeUiItem) {
            itemPromoBinding.item = promoCodeUiItem
            itemPromoBinding.executePendingBindings()
        }
    }

    class PromoCodeDiffUtil : DiffUtil.ItemCallback<PromoCodeUiItem>() {
        override fun areItemsTheSame(oldItem: PromoCodeUiItem, newItem: PromoCodeUiItem): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: PromoCodeUiItem, newItem: PromoCodeUiItem): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoCodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PromoCodeViewHolder(
            LayoutItemPromoBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PromoCodeViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.itemView.setOnClickListener {
            promoInteraction.onClick(getItem(position).code)
        }
    }
}