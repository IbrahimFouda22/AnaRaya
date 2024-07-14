package com.anaraya.anaraya.screens.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemAddressCartBinding
import com.anaraya.anaraya.screens.address.AddressUiStateData
import com.anaraya.anaraya.screens.cart.interaction.CartAddressInteraction


class CartAddressAdapter(private val interaction: CartAddressInteraction) :
    ListAdapter<AddressUiStateData, CartAddressAdapter.HomeAdsViewHolder>(
        HomeAdsDiffUtil()
    ) {
    class HomeAdsViewHolder(val layoutItemAddressCartBinding: LayoutItemAddressCartBinding) :
        RecyclerView.ViewHolder(layoutItemAddressCartBinding.root) {
        fun bind(addressUiStateData: AddressUiStateData) {
            layoutItemAddressCartBinding.item = addressUiStateData
            layoutItemAddressCartBinding.executePendingBindings()
        }
    }

    class HomeAdsDiffUtil : DiffUtil.ItemCallback<AddressUiStateData>() {
        override fun areItemsTheSame(
            oldItem: AddressUiStateData,
            newItem: AddressUiStateData
        ): Boolean {
            return oldItem.addressUiState.id == newItem.addressUiState.id
        }

        override fun areContentsTheSame(
            oldItem: AddressUiStateData,
            newItem: AddressUiStateData
        ): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeAdsViewHolder(LayoutItemAddressCartBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAdsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.layoutItemAddressCartBinding.radioAddressCart.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                if(!getItem(position).defaultAddress)
                    interaction.onClickAddress(getItem(position).addressUiState.id,position,getItem(position).isUserAddress)
            }
        }
    }
}
