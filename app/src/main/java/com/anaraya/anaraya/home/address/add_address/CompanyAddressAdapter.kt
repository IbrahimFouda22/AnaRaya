package com.anaraya.anaraya.home.address.add_address

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemCartBinding
import com.anaraya.anaraya.databinding.LayoutItemCompanyAddressBindBinding
import com.anaraya.anaraya.home.cart.CartUiList
import com.anaraya.anaraya.home.cart.interaction.CartInteraction
import com.chauthai.swipereveallayout.ViewBinderHelper


class CompanyAddressAdapter() :
    PagingDataAdapter<CompanyAddressUiItem, CompanyAddressAdapter.CompanyAddressViewHolder>(
        CompanyAddressDiffUtil()
    ) {

    class CompanyAddressViewHolder(private val layoutItemCompanyAddressBindBinding: LayoutItemCompanyAddressBindBinding) :
        RecyclerView.ViewHolder(layoutItemCompanyAddressBindBinding.root) {
//        fun bind(cartUiList: CartUiList) {
//            layoutItemCartBinding.item = cartUiList
//            layoutItemCartBinding.executePendingBindings()
//        }
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
        //holder.bind(getItem(position)!!)
    }

}