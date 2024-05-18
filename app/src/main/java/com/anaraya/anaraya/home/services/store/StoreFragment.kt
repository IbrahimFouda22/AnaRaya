package com.anaraya.anaraya.home.services.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.AuthAdapter
import com.anaraya.anaraya.databinding.FragmentStoreBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.home.services.store.explore.ExploreFragment
import com.anaraya.anaraya.home.services.store.my_items.MyItemsFragment
import com.anaraya.anaraya.home.services.store.sell.SellFragment
import com.anaraya.anaraya.util.showBottomNavBar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoreFragment : Fragment() {
    private lateinit var binding: FragmentStoreBinding
    private val navArgs by navArgs<StoreFragmentArgs>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStoreBinding.inflate(layoutInflater)

        binding.tabStore.addTab(binding.tabStore.newTab().setText(getString(R.string.explore)))
        binding.tabStore.addTab(binding.tabStore.newTab().setText(getString(R.string.sell)))
        binding.tabStore.addTab(binding.tabStore.newTab().setText(getString(R.string.my_items)))
        val list = arrayListOf(
            ExploreFragment(navArgs.isProducts),
            SellFragment(navArgs.isProducts),
            MyItemsFragment(navArgs.isProducts)
        )
        val adapter = AuthAdapter(list, childFragmentManager, lifecycle)
        binding.viewPagerStore.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if(it.navigateToSell){
                    findNavController().navigate(StoreFragmentDirections.actionStoreFragmentToEditItemServiceFragment(navArgs.isProducts))
                }
            }
        }

        val onPageSelected = getOnPageChangeCallBack()
        binding.tabStore.addOnTabSelectedListener(getTabOnSelectedListener())
        binding.viewPagerStore.registerOnPageChangeCallback(onPageSelected)
        return binding.root
    }

    private fun getTabOnSelectedListener(): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPagerStore.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        }
    }

    private fun getOnPageChangeCallBack(): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabStore.selectTab(binding.tabStore.getTabAt(position))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(),false)
        binding.name = if (navArgs.isProducts) getString(R.string.items) else getString(R.string.services)
    }
}