package com.anaraya.anaraya.screens.services.store.service.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.anaraya.anaraya.R
import com.anaraya.anaraya.databinding.FragmentExploreServiceBinding
import com.anaraya.anaraya.screens.activity.HomeActivityViewModel
import com.anaraya.anaraya.screens.services.store.adapter.ExploreCategoryAdapter
import com.anaraya.anaraya.screens.services.store.interaction.ExploreCategoryInteraction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreServiceFragment : Fragment(), ExploreCategoryInteraction {

    private lateinit var binding: FragmentExploreServiceBinding
    private val viewModel by viewModels<ExploreServiceViewModel>()
    private val sharedViewModel by viewModels<HomeActivityViewModel>({ requireActivity() })
    private lateinit var btnBack: ImageButton
    private lateinit var btnReload: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreServiceBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        btnBack = requireActivity().findViewById(R.id.btnBackHomeActivity)
        btnReload = requireActivity().findViewById(R.id.btnReload)

        val adapterCategory = ExploreCategoryAdapter(this)
        binding.recyclerExploreService.adapter = adapterCategory

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exploreUiState.collectLatest {
                if (it.error != null) {
                    sharedViewModel.setError(error = it.error)
                    if (it.error != getString(R.string.no_internet))
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
                if (it.exploreCategoriesList.isNotEmpty()) {
                    adapterCategory.submitList(it.exploreCategoriesList)
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
        viewModel.getAllData()
    }

    private fun reload() {
        sharedViewModel.reloadClick()
        viewModel.getAllData()
        sharedViewModel.reloadClickDone()
    }

    override fun onClick(catId: Int, catName: String) {
        sharedViewModel.navigateToExploreService(catId, catName)
    }
}