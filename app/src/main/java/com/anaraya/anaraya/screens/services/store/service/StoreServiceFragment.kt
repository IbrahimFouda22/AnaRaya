package com.anaraya.anaraya.screens.services.store.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.anaraya.anaraya.R
import com.anaraya.anaraya.authentication.AuthAdapter
import com.anaraya.anaraya.databinding.FragmentStoreServiceBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.service.explore.ExploreServiceFragment
import com.anaraya.anaraya.screens.services.store.service.my_items.MyItemsServicesFragment
import com.anaraya.anaraya.screens.services.store.service.sell.SellServicesFragment
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoreServiceFragment : Fragment() {
    private lateinit var binding: FragmentStoreServiceBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStoreServiceBinding.inflate(layoutInflater)

        binding.tabStoreService.addTab(binding.tabStoreService.newTab().setText(getString(R.string.explore)))
        binding.tabStoreService.addTab(binding.tabStoreService.newTab().setText(getString(R.string.add_yours)))
        binding.tabStoreService.addTab(binding.tabStoreService.newTab().setText(getString(R.string.my_items)))
        val list = arrayListOf(
            ExploreServiceFragment(),
            SellServicesFragment(),
            MyItemsServicesFragment()
        )
        val adapter = AuthAdapter(list, childFragmentManager, lifecycle)
        binding.viewPagerStoreService.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.homeState.collectLatest {
                if (it.navigateToExploreService) {
                    findNavController().navigate(
                        StoreServiceFragmentDirections.actionStoreServiceFragmentToExploreServicesFragment(
                            categoryId = sharedViewModel.homeState.value.exploreSubCatId,
                            categoryName = sharedViewModel.homeState.value.exploreSubCatName,
                        )
                    )
                    sharedViewModel.navigateToExploreServiceDone()
                }
                if (it.navigateToTermsAndCondition) {
                    findNavController().navigate(
                        StoreServiceFragmentDirections.actionStoreServiceFragmentToTermsAndConditionFragment()
                    )
                    sharedViewModel.navigateToTermsAndConditionDone()
                }
            }
        }

        val onPageSelected = getOnPageChangeCallBack()
        binding.tabStoreService.addOnTabSelectedListener(getTabOnSelectedListener())
        binding.viewPagerStoreService.registerOnPageChangeCallback(onPageSelected)

        return binding.root
    }

    private fun getTabOnSelectedListener(): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPagerStoreService.currentItem = tab.position
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
                binding.tabStoreService.selectTab(binding.tabStoreService.getTabAt(position))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(), false)
        showToolBar(requireActivity(), false)
        showCardHome(requireActivity(),false)
    }
}