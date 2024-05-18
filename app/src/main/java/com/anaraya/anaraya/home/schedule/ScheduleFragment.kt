package com.anaraya.anaraya.home.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentScheduleBinding
import com.anaraya.anaraya.home.activity.HomeActivityViewModel
import com.anaraya.anaraya.util.gone
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel by viewModels<ScheduleViewModel>({this})

    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        lifecycleScope.launch {
            viewModel.scheduleUiState.collectLatest{
                if (it.error != null) {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    sharedViewModel.setError(it.error)
                }
                if (it.navigateToBack){
                    findNavController().popBackStack()
                    viewModel.navigateToBackDone()
                }
            }
        }

        btnBack.setOnClickListener {
            reload()
        }
        btnReload.setOnClickListener {
            reload()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val cardSearch = requireActivity().findViewById<CardView>(R.id.cardSearch_homeActivity)
        val bottomNav = requireActivity().findViewById<ChipNavigationBar>(R.id.bottomNavHome)
        cardSearch.gone()
        bottomNav.gone()
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

}