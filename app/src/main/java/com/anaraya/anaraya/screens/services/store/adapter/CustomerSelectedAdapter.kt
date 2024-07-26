package com.anaraya.anaraya.screens.services.store.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemCustomerSelectedBinding
import com.anaraya.anaraya.screens.services.store.interaction.CustomerInteraction
import com.anaraya.anaraya.screens.services.store.service.my_items.ServiceCustomerInformationUiState


class CustomerSelectedAdapter(private val interAction: CustomerInteraction) :
    ListAdapter<ServiceCustomerInformationUiState, CustomerSelectedAdapter.CompanySelectedViewHolder>(
        CompanySelectedDiffUtil()
    ) {
    inner class CompanySelectedViewHolder(val layoutItemCustomerSelectedBinding: LayoutItemCustomerSelectedBinding) :
        RecyclerView.ViewHolder(layoutItemCustomerSelectedBinding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: ServiceCustomerInformationUiState) {
            layoutItemCustomerSelectedBinding.item = item
            layoutItemCustomerSelectedBinding.consCustomerSelected.setBackgroundResource(
                if (item.isSelected) R.drawable.shape_item_selected else R.drawable.shape_item_un_selected
            )
            layoutItemCustomerSelectedBinding.executePendingBindings()
        }
    }

    class CompanySelectedDiffUtil : DiffUtil.ItemCallback<ServiceCustomerInformationUiState>() {
        override fun areItemsTheSame(
            oldItem: ServiceCustomerInformationUiState,
            newItem: ServiceCustomerInformationUiState,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ServiceCustomerInformationUiState,
            newItem: ServiceCustomerInformationUiState,
        ): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CompanySelectedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CompanySelectedViewHolder(
            LayoutItemCustomerSelectedBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CompanySelectedViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null){
            holder.bind(item)
        }
        holder.layoutItemCustomerSelectedBinding.consCustomerSelected.setOnClickListener {
            interAction.onClickCustomer(getItem(position).listeningId,position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeSelected(position: Int, isSelect: Boolean) {
        getItem(position)!!.isSelected = isSelect
        notifyItemChanged(position)
    }
}