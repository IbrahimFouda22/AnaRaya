package com.anaraya.anaraya.home.total_cost.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemCheckOutBinding
import com.anaraya.anaraya.home.cart.CartUiList


class CheckOutAdapter :
    ListAdapter<CartUiList, CheckOutAdapter.CartViewHolder>(
        CartDiffUtil()
    ) {

    class CartViewHolder(private val layoutItemCheckOutBinding: LayoutItemCheckOutBinding) :
        RecyclerView.ViewHolder(layoutItemCheckOutBinding.root) {

        fun bind(cartUiList: CartUiList) {
            layoutItemCheckOutBinding.item = cartUiList
            layoutItemCheckOutBinding.executePendingBindings()
        }
    }

    class CartDiffUtil : DiffUtil.ItemCallback<CartUiList>() {
        override fun areItemsTheSame(oldItem: CartUiList, newItem: CartUiList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartUiList, newItem: CartUiList): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CartViewHolder(
            LayoutItemCheckOutBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

}