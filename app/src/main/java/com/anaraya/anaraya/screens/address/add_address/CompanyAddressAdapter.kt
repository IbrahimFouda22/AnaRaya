package com.anaraya.anaraya.screens.address.add_address

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemCompanyAddressBindBinding
import java.text.SimpleDateFormat
import java.util.Date


class CompanyAddressAdapter() :
    PagingDataAdapter<CompanyAddressUiItem, CompanyAddressAdapter.CompanyAddressViewHolder>(
        CompanyAddressDiffUtil()
    ) {

    class CompanyAddressViewHolder(private val layoutItemCompanyAddressBindBinding: LayoutItemCompanyAddressBindBinding) :
        RecyclerView.ViewHolder(layoutItemCompanyAddressBindBinding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(companyAddressUiItem: CompanyAddressUiItem) {
            val day = when(companyAddressUiItem.dayOfDelivery){
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
            layoutItemCompanyAddressBindBinding.item = "${companyAddressUiItem.office} : $day ${from?.toDateString()} to ${to?.toDateString()}"
            layoutItemCompanyAddressBindBinding.executePendingBindings()
        }
    }

    class CompanyAddressDiffUtil : DiffUtil.ItemCallback<CompanyAddressUiItem>() {
        override fun areItemsTheSame(oldItem: CompanyAddressUiItem, newItem: CompanyAddressUiItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CompanyAddressUiItem, newItem: CompanyAddressUiItem): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyAddressViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanyAddressViewHolder(
            LayoutItemCompanyAddressBindBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CompanyAddressViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

}

@SuppressLint("SimpleDateFormat")
private fun Date.toDateString():String = SimpleDateFormat("HH:mm").format(this)