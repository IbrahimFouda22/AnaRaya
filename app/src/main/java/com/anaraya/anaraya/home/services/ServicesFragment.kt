package com.anaraya.anaraya.home.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.databinding.FragmentServicesBinding
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServicesFragment : Fragment() {
    private lateinit var binding: FragmentServicesBinding
    private val viewModel by viewModels<ServicesViewModel>({this})
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentServicesBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.servicesUiState.collectLatest {
                if(it.navigateToItems) {
                    findNavController().navigate(ServicesFragmentDirections.actionServicesFragmentToStoreFragment(true))
                    viewModel.navigateToItemsDone()
                }
                if(it.navigateToService) {
                    findNavController().navigate(ServicesFragmentDirections.actionServicesFragmentToStoreFragment(false))
                    viewModel.navigateToServiceDone()
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(), false)
        showCardHome(requireActivity(),false)
    }

}