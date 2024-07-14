package com.anaraya.anaraya.screens.address.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemAddressBinding
import com.anaraya.anaraya.screens.address.AddressUiStateData
import com.anaraya.anaraya.screens.address.interaction.AddressInteraction


class AddressAdapter(private val addressInteraction: AddressInteraction) :
    ListAdapter<AddressUiStateData, AddressAdapter.HomeAdsViewHolder>(
        HomeAdsDiffUtil()
    ) {
    class HomeAdsViewHolder(private val layoutItemAddressBinding: LayoutItemAddressBinding) :
        RecyclerView.ViewHolder(layoutItemAddressBinding.root) {
        val layout = layoutItemAddressBinding.swipeRevealLayout
        private val delLayout = layoutItemAddressBinding.delLayout
        val delBtn: ImageButton = delLayout.findViewById(R.id.btnDeleteAddress)

        private val layoutData = layoutItemAddressBinding.dataLayout
        val txtChangeAddress: TextView = layoutData.findViewById(R.id.txtChangeAddress)
        val switchDefault: SwitchCompat = layoutData.findViewById(R.id.switchDefault)

        fun bind(addressUiStateData: AddressUiStateData) {
            layoutItemAddressBinding.item = addressUiStateData
            layoutItemAddressBinding.executePendingBindings()
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
            return oldItem.defaultAddress == newItem.defaultAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeAdsViewHolder(LayoutItemAddressBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAdsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.txtChangeAddress.setOnClickListener {
            addressInteraction.onClickChange(getItem(position).addressUiState)
        }

        holder.switchDefault.setOnClickListener {
            if (itemCount > 1) {
                addressInteraction.onClickSwitch(
                    getItem(position).addressUiState.id,
                    getItem(position).isUserAddress
                )
            } else {
                holder.switchDefault.isChecked =
                    !holder.switchDefault.isChecked
                addressInteraction.onClickSwitch(
                    "-1",
                    getItem(position).isUserAddress
                )
            }
            //getItem(position).defaultAddress = isChecked
            //notifyItemChanged(position)
        }
        holder.delBtn.setOnClickListener {
            addressInteraction.onClickDelete(getItem(position).addressUiState.id)
        }
    }
}
