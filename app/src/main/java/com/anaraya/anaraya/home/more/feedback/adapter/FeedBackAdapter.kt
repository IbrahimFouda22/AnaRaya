package com.anaraya.anaraya.home.more.feedback.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FeedBackAdapter(private val list: List<Fragment>,private val fm:FragmentManager, lifeCycle:Lifecycle):FragmentStateAdapter(fm,lifeCycle) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}