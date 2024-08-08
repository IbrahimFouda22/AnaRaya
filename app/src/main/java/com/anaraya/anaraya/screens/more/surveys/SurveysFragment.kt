package com.anaraya.anaraya.screens.more.surveys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentSurveysBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.more.surveys.adapter.SurveysAdapter
import com.anaraya.anaraya.screens.more.surveys.interaction.SurveysInteraction
import com.anaraya.anaraya.util.showBottomNavBar
import com.anaraya.anaraya.util.showCardHome
import com.anaraya.anaraya.util.showToolBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SurveysFragment : Fragment(), SurveysInteraction {
    private lateinit var binding: FragmentSurveysBinding
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    private val viewModel by viewModels<SurveysViewModel>({ this })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSurveysBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapter = SurveysAdapter(this)
        binding.recyclerSurveys.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.surveysUiState.collectLatest {
                if (!it.error.isNullOrEmpty()) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet) && it.error.isNotEmpty())
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.surveys.isNotEmpty()){
                    adapter.submitList(it.surveys)
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

    override fun onClick(surveyId: Int, title: String) {
        findNavController().navigate(SurveysFragmentDirections.actionSurveysFragmentToSurveyDetailsFragment(surveyId,title))
    }

    override fun onStart() {
        super.onStart()
        showToolBar(requireActivity(),true)
        showBottomNavBar(requireActivity(),false)
        showCardHome(requireActivity(),false)
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        showToolBar(requireActivity(),true)
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }
}