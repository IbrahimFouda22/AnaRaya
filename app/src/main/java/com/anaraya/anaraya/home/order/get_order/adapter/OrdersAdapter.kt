package com.anaraya.anaraya.home.order.get_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemOrderDetailsBinding
import com.anaraya.anaraya.home.order.get_order.OrderUiStateData
import com.chauthai.swipereveallayout.ViewBinderHelper


class OrdersAdapter :
    ListAdapter<OrderUiStateData, OrdersAdapter.OrderViewHolder>(
        OrderDiffUtil()
    ) {
    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    class OrderViewHolder(private val layoutItemOrderBinding: LayoutItemOrderDetailsBinding) :
        RecyclerView.ViewHolder(layoutItemOrderBinding.root) {

        val layout = layoutItemOrderBinding.swipeRevealLayout
        val dataLayout = layoutItemOrderBinding.dataLayout
        private val delLayout = layoutItemOrderBinding.delLayout
        val delBtn: ImageButton = delLayout.findViewById(R.id.btnDeleteOrder)
        fun bind(orderUiStateData: OrderUiStateData) {
            layoutItemOrderBinding.item = orderUiStateData
            layoutItemOrderBinding.executePendingBindings()
        }
    }

    class OrderDiffUtil : DiffUtil.ItemCallback<OrderUiStateData>() {
        override fun areItemsTheSame(oldItem: OrderUiStateData, newItem: OrderUiStateData): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderUiStateData, newItem: OrderUiStateData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(
            LayoutItemOrderDetailsBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        viewBinderHelper.bind(holder.layout,getItem(position).orderId.toString())
        if (!getItem(position).cancellable)
            viewBinderHelper.lockSwipe(getItem(position).orderId.toString())
        holder.bind(getItem(position)!!)
        holder.delBtn.setOnClickListener {
        }
        holder.dataLayout.setOnClickListener {
            getItem(position).expanded = !getItem(position).expanded
            notifyItemChanged(position)
        }
    }

}