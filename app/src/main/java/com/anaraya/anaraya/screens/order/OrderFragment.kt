package com.anaraya.anaraya.screens.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.databinding.FragmentOrderBinding
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private val viewModel by viewModels<OrderViewModel>({this})
    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            viewModel.navigateToHome.collectLatest {
                if(it){
                    findNavController().navigate(OrderFragmentDirections.actionGlobalHomeFragment())
                    viewModel.navigateToHomerDone()
                }
            }
        }

        binding.btnBackToHomeOrder.setOnClickListener {
            viewModel.navigateToHome()
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(),false)
    }
}