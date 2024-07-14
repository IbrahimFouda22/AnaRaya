package com.anaraya.anaraya.screens.services

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.databinding.FragmentServicesBinding
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServicesFragment : Fragment() {
    private lateinit var binding: FragmentServicesBinding
    private val viewModel by viewModels<ServicesViewModel>({this})
    @Inject
    lateinit var sharedPreferences: SharedPreferences
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
                    if(!sharedPreferences.getBoolean("isFamily",false))
                        findNavController().navigate(ServicesFragmentDirections.actionServicesFragmentToStoreFragment())
                    else
                        Toast.makeText(requireContext(), "Unavailable", Toast.LENGTH_SHORT).show()
                    viewModel.navigateToItemsDone()
                }
                if(it.navigateToService) {
                    if(!sharedPreferences.getBoolean("isFamily",false))
                        findNavController().navigate(ServicesFragmentDirections.actionServicesFragmentToStoreServiceFragment())
                    else
                        Toast.makeText(requireContext(), "Unavailable", Toast.LENGTH_SHORT).show()
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
        showBottomNavBar(requireActivity(),true)
    }

}