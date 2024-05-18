package com.anaraya.anaraya.home.more.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.anaraya.anaraya.databinding.FragmentFeedBackBinding
import com.anaraya.anaraya.home.more.feedback.adapter.FeedBackAdapter
import com.anaraya.anaraya.home.more.feedback.question1.Question1Fragment
import com.anaraya.anaraya.home.more.feedback.question2.Question2Fragment
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showToolBar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedBackFragment : Fragment() {

    private lateinit var binding: FragmentFeedBackBinding
    private val viewModel by viewModels<FeedBackViewModel>({ requireActivity() })
    private lateinit var list: List<Fragment>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeedBackBinding.inflate(layoutInflater)
        binding.tabFeedBack.addTab(binding.tabFeedBack.newTab().setText("QUESTION1"))
        binding.tabFeedBack.addTab(binding.tabFeedBack.newTab().setText("QUESTION2"))

        list = arrayListOf(
            Question1Fragment(),
            Question2Fragment()
        )
        val adapter = FeedBackAdapter(list, childFragmentManager, lifecycle)
        binding.viewPagerFeedBack.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.feedBackState.collectLatest {
                if(it.stateViewPager > -1)
                    binding.viewPagerFeedBack.currentItem = it.stateViewPager
            }
        }
        binding.viewPagerFeedBack.registerOnPageChangeCallback(onViewPagerChange())
        binding.tabFeedBack.addOnTabSelectedListener(onTabSelectedChange())

        return binding.root
    }

    private fun onViewPagerChange() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tabFeedBack.selectTab(binding.tabFeedBack.getTabAt(position))
        }
    }

    private fun onTabSelectedChange() = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if(tab!=null){
                binding.viewPagerFeedBack.currentItem = tab.position
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

    }

    override fun onStart() {
        super.onStart()
        showBottomNavBar(requireActivity(),false)
        showToolBar(requireActivity(),true)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.viewPagerFeedBack.unregisterOnPageChangeCallback(onViewPagerChange())
    }

}