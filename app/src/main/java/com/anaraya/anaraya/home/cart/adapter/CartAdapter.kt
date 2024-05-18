package com.anaraya.anaraya.home.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.LayoutItemCartBinding
import com.anaraya.anaraya.home.cart.CartUiList
import com.anaraya.anaraya.home.cart.interaction.CartInteraction
import com.chauthai.swipereveallayout.ViewBinderHelper


class CartAdapter(private val interaction: CartInteraction) :
    ListAdapter<CartUiList, CartAdapter.CartViewHolder>(
        CartDiffUtil()
    ) {
    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    class CartViewHolder(private val layoutItemCartBinding: LayoutItemCartBinding) :
        RecyclerView.ViewHolder(layoutItemCartBinding.root) {
        val layout = layoutItemCartBinding.swipeRevealLayout
        private val delLayout = layoutItemCartBinding.delLayout
        val delBtn: ImageButton = delLayout.findViewById(R.id.btnDeleteCart)


        private val layoutData = layoutItemCartBinding.dataLayout
        val btnPlus: ImageButton = layoutData.findViewById(R.id.btnPlusCart)
        val btnMinus: ImageButton = layoutData.findViewById(R.id.btnMinusCart)

        fun bind(cartUiList: CartUiList) {
            layoutItemCartBinding.item = cartUiList
            layoutItemCartBinding.executePendingBindings()
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
            LayoutItemCartBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        viewBinderHelper.bind(holder.layout, getItem(position)!!.id.toString())
        //viewBinderHelper.lockSwipe(getItem(0).id.toString())
        holder.bind(getItem(position)!!)
        holder.delBtn.setOnClickListener {
            interaction.onClickDelete(getItem(position)!!.id, position)
        }
        holder.btnPlus.setOnClickListener {
            getItem(position).plus = false
            notifyItemChanged(position)
            interaction.onClickPlus(getItem(position).id, getItem(position).qty + 1)
        }

        holder.btnMinus.setOnClickListener {
            getItem(position).minus = false
            notifyItemChanged(position)
            interaction.onClickMinus(getItem(position).id, getItem(position).qty - 1)
        }

    }

}