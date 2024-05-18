package com.anaraya.anaraya.home.more.family.family_referrals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaraya.anaraya.databinding.LayoutItemReferralBinding


class FamilyReferralsAdapter() :
    ListAdapter<ReferralsItemUiState, FamilyReferralsAdapter.CartViewHolder>(
        CartDiffUtil()
    ) {

    class CartViewHolder(private val layoutItemReferralBinding: LayoutItemReferralBinding) :
        RecyclerView.ViewHolder(layoutItemReferralBinding.root) {
        fun bind(referralsItemUiState: ReferralsItemUiState) {
            layoutItemReferralBinding.item = referralsItemUiState
            layoutItemReferralBinding.executePendingBindings()
        }
    }

    class CartDiffUtil : DiffUtil.ItemCallback<ReferralsItemUiState>() {
        override fun areItemsTheSame(
            oldItem: ReferralsItemUiState,
            newItem: ReferralsItemUiState
        ): Boolean {
            return oldItem.referralPhoneNumber == newItem.referralPhoneNumber
        }

        override fun areContentsTheSame(
            oldItem: ReferralsItemUiState,
            newItem: ReferralsItemUiState
        ): Boolean {
            return false
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CartViewHolder(
            LayoutItemReferralBinding.inflate(
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