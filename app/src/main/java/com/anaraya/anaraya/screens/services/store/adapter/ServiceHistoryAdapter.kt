package com.anaraya.anaraya.screens.services.store.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemServiceHistoryBinding
import com.anaraya.anaraya.screens.services.store.interaction.CustomerInteraction
import com.anaraya.anaraya.screens.services.store.interaction.NumberInteraction
import com.anaraya.anaraya.screens.services.store.service.my_items.ServiceCustomerInformationUiState


class ServiceHistoryAdapter(
    private val interAction: NumberInteraction,
) :
    ListAdapter<ServiceCustomerInformationUiState, ServiceHistoryAdapter.CServiceHistoryViewHolder>(
        ServiceHistoryDiffUtil()
    ) {
    inner class CServiceHistoryViewHolder(private val layoutItemServiceHistoryBinding: LayoutItemServiceHistoryBinding) :
        RecyclerView.ViewHolder(layoutItemServiceHistoryBinding.root) {
        val txtNumber: TextView =
            layoutItemServiceHistoryBinding.consCustomerSelected.findViewById(R.id.txtPhoneValue)

        @SuppressLint("SimpleDateFormat")
        fun bind(item: ServiceCustomerInformationUiState) {
            layoutItemServiceHistoryBinding.item = item
            layoutItemServiceHistoryBinding.executePendingBindings()
        }
    }

    class ServiceHistoryDiffUtil : DiffUtil.ItemCallback<ServiceCustomerInformationUiState>() {
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
    ): CServiceHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CServiceHistoryViewHolder(
            LayoutItemServiceHistoryBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CServiceHistoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.txtNumber.setOnLongClickListener {
            interAction.onClickNumberValue(holder.txtNumber.text.toString())
            true
        }
    }

}