package com.anaraya.anaraya.screens.services.store.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemCompanyAddressBindSelectedBinding
import com.anaraya.anaraya.screens.address.add_address.CompanyAddressUiItem
import com.anaraya.anaraya.screens.address.add_address.toDateString
import com.anaraya.anaraya.screens.services.store.interaction.AddressesInteraction
import java.text.SimpleDateFormat


class CompanyAddressSelectedAdapter(private val interAction: AddressesInteraction) :
    PagingDataAdapter<CompanyAddressUiItem, CompanyAddressSelectedAdapter.CompanyAddressViewHolder>(
        CompanyAddressDiffUtil()
    ) {
    inner class CompanyAddressViewHolder(val layoutItemCompanyAddressBindBinding: LayoutItemCompanyAddressBindSelectedBinding) :
        RecyclerView.ViewHolder(layoutItemCompanyAddressBindBinding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(companyAddressUiItem: CompanyAddressUiItem) {
            val day = when (companyAddressUiItem.dayOfDelivery) {
                0 -> "Saturday"
                1 -> "Sunday"
                2 -> "Monday"
                3 -> "Tuesday"
                4 -> "Wednesday"
                5 -> "Thursday"
                else -> "Friday"
            }
            val from = SimpleDateFormat("HH:mm").parse(companyAddressUiItem.deliveryFrom)
            val to = SimpleDateFormat("HH:mm").parse(companyAddressUiItem.deliveryTo)
            layoutItemCompanyAddressBindBinding.item =
                "${companyAddressUiItem.office} : $day ${from?.toDateString()} to ${to?.toDateString()}"
            layoutItemCompanyAddressBindBinding.txtCompanySelected.setBackgroundResource(
                if (companyAddressUiItem.isSelected) R.drawable.shape_item_selected else R.drawable.shape_item_un_selected
            )
            layoutItemCompanyAddressBindBinding.executePendingBindings()
        }
    }

    class CompanyAddressDiffUtil : DiffUtil.ItemCallback<CompanyAddressUiItem>() {
        override fun areItemsTheSame(
            oldItem: CompanyAddressUiItem,
            newItem: CompanyAddressUiItem,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CompanyAddressUiItem,
            newItem: CompanyAddressUiItem,
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CompanyAddressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanyAddressViewHolder(
            LayoutItemCompanyAddressBindSelectedBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CompanyAddressViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null){
            holder.bind(item)
        }
        holder.layoutItemCompanyAddressBindBinding.txtCompanySelected.setOnClickListener {
                interAction.onClick(getItem(holder.bindingAdapterPosition)!!.id,position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelected(position: Int, isSelect: Boolean) {
        getItem(position)!!.isSelected = isSelect
        notifyItemChanged(position)
    }
}